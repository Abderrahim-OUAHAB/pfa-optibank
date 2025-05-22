import logging
from cassandra.auth import PlainTextAuthProvider
from cassandra.cluster import Cluster
from pyspark.sql import DataFrame

logger = logging.getLogger(__name__)

class CassandraConnector:
    """Gère l'insertion dans Cassandra"""
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
            
            # Table pour les transactions légitimes
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
            
            # Table pour les transactions frauduleuses
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
            
            logger.info(f"Keyspace {self.keyspace} et tables créés avec succès")
        except Exception as e:
            logger.error(f"Erreur lors de la création du keyspace/tables: {str(e)}", exc_info=True)
            raise

    def update_transaction_status(self, transaction_id: str, is_fraud: bool):
        """
        Met à jour le statut d'une transaction dans la table 'transactions'
        """
        try:
            status = "REJECTED" if is_fraud else "APPROVED"
            query = f"""
                UPDATE {self.keyspace}.transactions 
                SET status = '{status}' 
                WHERE transaction_id = '{transaction_id}'
            """
            self.session.execute(query)
            logger.info(f"Statut mis à jour pour la transaction {transaction_id} : {status}")
        except Exception as e:
            logger.error(f"Erreur lors de la mise à jour du statut pour {transaction_id}: {e}", exc_info=True)
        
    def write_transactions(self, df: DataFrame, is_fraud: bool) -> bool:
        """Écrit un DataFrame dans la table appropriée"""
        try:
            if df.rdd.isEmpty():
                logger.info("Batch vide, aucune écriture nécessaire")
                return False
            
            # Sélectionner uniquement les colonnes existantes dans la table Cassandra
            existing_columns = [
                "transactionid", "accountid", "transactionamount", "transactiondate",
                "transactiontype", "location", "deviceid", "ipaddress", "merchantid",
                "accountbalance", "previoustransactiondate", "channel", "customerage",
                "customeroccupation", "transactionduration", "loginattempts", "is_fraud"
            ]
            
            # Filtrer les colonnes du DataFrame pour ne garder que celles qui existent dans la table
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
            logger.error(f"Erreur lors de l'écriture dans Cassandra: {str(e)}", exc_info=True)
            return False