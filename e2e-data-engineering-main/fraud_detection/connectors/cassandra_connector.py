import logging
import uuid
from cassandra.cluster import Cluster
from pyspark.sql import DataFrame

logger = logging.getLogger(__name__)

class CassandraConnector:
    def __init__(self, keyspace: str):
        self.keyspace = keyspace
        self.cluster = None
        self.session = None
        self.connect()
        self.setup_schema()

    def connect(self):
        try:
            self.cluster = Cluster(["localhost"])
            self.session = self.cluster.connect()
            logger.info("Connexion Cassandra établie avec succès")
        except Exception as e:
            logger.error(f"Erreur de connexion Cassandra: {str(e)}", exc_info=True)
            raise

    def setup_schema(self):
        try:
            self.session.execute(f"""
                CREATE KEYSPACE IF NOT EXISTS {self.keyspace}
                WITH replication = {{'class': 'SimpleStrategy', 'replication_factor': 1}}
            """)
            self.session.set_keyspace(self.keyspace)

            self.session.execute("""
                CREATE TABLE IF NOT EXISTS legitimate_transactions (
                    transactionid TEXT PRIMARY KEY,
                    accountid TEXT,
                    transactionamount DOUBLE,
                    transactiondate TIMESTAMP,
                    transactiontype TEXT,
                    location TEXT,
                    deviceid TEXT,
                    ipaddress TEXT,
                    merchantid TEXT,
                    accountbalance DOUBLE,
                    previoustransactiondate TIMESTAMP,
                    channel TEXT,
                    customerage INT,
                    customeroccupation TEXT,
                    transactionduration INT,
                    loginattempts INT,
                    is_fraud BOOLEAN
                )
            """)

            self.session.execute("""
                CREATE TABLE IF NOT EXISTS fraudulent_transactions (
                    transactionid TEXT PRIMARY KEY,
                    accountid TEXT,
                    transactionamount DOUBLE,
                    transactiondate TIMESTAMP,
                    transactiontype TEXT,
                    location TEXT,
                    deviceid TEXT,
                    ipaddress TEXT,
                    merchantid TEXT,
                    accountbalance DOUBLE,
                    previoustransactiondate TIMESTAMP,
                    channel TEXT,
                    customerage INT,
                    customeroccupation TEXT,
                    transactionduration INT,
                    loginattempts INT,
                    is_fraud BOOLEAN
                )
            """)

            self.session.execute("""
                CREATE TABLE IF NOT EXISTS alerts (
                    alertid TEXT PRIMARY KEY,
                    accountid TEXT,
                    type TEXT,
                    message TEXT,
                    severity TEXT,
                    status TEXT
                )
            """)

            logger.info(f"Keyspace {self.keyspace} et tables créés avec succès")
        except Exception as e:
            logger.error(f"Erreur lors de la création du keyspace/tables: {str(e)}", exc_info=True)
            raise

    def update_transaction_status(self, transaction_id: str, is_fraud: bool, transaction_amount: float, transaction_type: str):
        try:
            status = "REJECTED" if is_fraud else "APPROVED"
            
            # D'abord mettre à jour le statut de la transaction
            self.session.execute(f"""
                UPDATE {self.keyspace}.transactions 
                SET status = '{status}' 
                WHERE transaction_id = '{transaction_id}'
            """)
            
            # Si la transaction est approuvée, mettre à jour le solde
            if not is_fraud:
                # Récupérer le compte associé
                result = self.session.execute(f"""
                    SELECT account_id FROM {self.keyspace}.transactions
                    WHERE transaction_id = '{transaction_id}'
                """)
                account_id = result.one().account_id
                
                # Déterminer l'opération selon le type de transaction
                operation = "+" if transaction_type.lower() == "credit" else "-"
                ancienne_balance = self.session.execute(f"""
                    SELECT balance FROM {self.keyspace}.accounts
                    WHERE accountid = '{account_id}'
                """).one().balance
                
                nvbalance=0
                if(operation == "-"):
                    nvbalance = ancienne_balance - transaction_amount
                else:
                    nvbalance = ancienne_balance + transaction_amount
                # Mettre à jour le solde
                self.session.execute(f"""
                    UPDATE {self.keyspace}.accounts
                    SET balance = {nvbalance}
                    WHERE accountid = '{account_id}'
                """)
            
            logger.info(f"Statut mis à jour pour la transaction {transaction_id} : {status}")
            logger.info(f"Solde mis à jour pour le compte {account_id} avec opération {operation}{transaction_amount}")
        except Exception as e:
            logger.error(f"Erreur lors de la mise à jour du statut/solde: {str(e)}", exc_info=True)

    def write_transactions(self, df: DataFrame, is_fraud: bool) -> bool:
        try:
            if df.rdd.isEmpty():
                logger.info("Batch vide, aucune écriture nécessaire")
                return False

            existing_columns = [
                "transactionid", "accountid", "transactionamount", "transactiondate",
                "transactiontype", "location", "deviceid", "ipaddress", "merchantid",
                "accountbalance", "previoustransactiondate", "channel", "customerage",
                "customeroccupation", "transactionduration", "loginattempts", "is_fraud"
            ]

            df_to_write = df.select([col for col in existing_columns if col in df.columns])
            table_name = "fraudulent_transactions" if is_fraud else "legitimate_transactions"

            df_to_write.write \
                .format("org.apache.spark.sql.cassandra") \
                .mode("append") \
                .options(table=table_name, keyspace=self.keyspace) \
                .save()

            logger.info(f"{df_to_write.count()} transactions écrites dans {table_name}")
            return True
        except Exception as e:
            logger.error(f"Erreur lors de l'écriture: {str(e)}", exc_info=True)
            return False

    def create_alert(self, account_id: str, message: str, severity: str = "HIGH", status: str = "UNREAD"):
        try:
            alert_id = str(uuid.uuid4())
            self.session.execute(f"""
                INSERT INTO {self.keyspace}.alerts (
                    alertid, accountid, type, message, severity, status
                ) VALUES (%s, %s, %s, %s, %s, %s)
            """, (alert_id, account_id, "FRAUD", message, "HIGH", "UNREAD"))
            logger.info(f"Alerte créée pour compte {account_id}")
        except Exception as e:
            logger.error(f"Erreur lors de la création de l'alerte: {e}", exc_info=True)

    def check_alerts_and_suspend_user(self, account_id: str):
        try:
            # Récupère l'email depuis la transaction
            email_result = self.session.execute(f"""
                SELECT user_email FROM transactions WHERE account_id = %s LIMIT 1 ALLOW FILTERING
            """, (account_id,))
            email_row = email_result.one()
            if not email_row:
                logger.warning(f"Aucun utilisateur trouvé pour compte {account_id}")
                return

            user_email = email_row.user_email

            # Compte le nombre d'alertes
            result = self.session.execute(f"""
                SELECT COUNT(*) FROM alerts WHERE accountid = %s ALLOW FILTERING
            """, (account_id,))
            alert_count = result.one()[0]

            if alert_count >= 5:
                self.session.execute(f"""
                    UPDATE users SET status = 'SUSPENDED' WHERE email = %s
                """, (user_email,))
                logger.info(f"Utilisateur {user_email} suspendu suite à {alert_count} alertes")
        except Exception as e:
            logger.error(f"Erreur dans la suspension utilisateur: {e}", exc_info=True)
