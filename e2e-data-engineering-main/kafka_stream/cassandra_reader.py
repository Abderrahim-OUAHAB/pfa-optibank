import logging
from datetime import datetime
from cassandra.cluster import Cluster
from cassandra.policies import RoundRobinPolicy

class CassandraStreamReader:
    def __init__(self, keyspace='spark_streams', table='transactions'):
        self.cluster = Cluster(
            ['localhost'],
            protocol_version=4,
            load_balancing_policy=RoundRobinPolicy()
        )
        try:
            self.session = self.cluster.connect()
            self._initialize_keyspace(keyspace)
            self.session.set_keyspace(keyspace)
            self._initialize_tables()
            self.keyspace = keyspace
            self.table = table
            self.processed_transactions = set()
            self._load_processed_transactions()
        except Exception as e:
            self.cluster.shutdown()
            raise RuntimeError(f"Échec de la connexion à Cassandra: {str(e)}")

    def _initialize_keyspace(self, keyspace):
        self.session.execute(f"""
            CREATE KEYSPACE IF NOT EXISTS {keyspace}
            WITH replication = {{'class': 'SimpleStrategy', 'replication_factor': 1}}
        """)

    def _initialize_tables(self):
        self.session.execute("""
            CREATE TABLE IF NOT EXISTS processed_transactions (
                transaction_id TEXT PRIMARY KEY,
                processed_at TIMESTAMP
            )
        """)

    def _load_processed_transactions(self):
        try:
            rows = self.session.execute("SELECT transaction_id FROM processed_transactions")
            self.processed_transactions = {str(row.transaction_id) for row in rows}
        except Exception as e:
            logging.error(f"Erreur chargement transactions traitées: {str(e)}")
            self.processed_transactions = set()

    def get_new_transactions(self):
        try:
            query = f"""
                SELECT * FROM {self.table}
                WHERE status = 'PENDING'
                LIMIT 100
                ALLOW FILTERING
            """
            rows = self.session.execute(query)
            new_transactions = []

            for row in rows:
                transaction_id = str(row.transaction_id)
                if transaction_id not in self.processed_transactions:
                    transaction = {
                        "transactionid": transaction_id,
                        "accountid": str(row.account_id),
                        "transactionamount": float(row.transaction_amount),
                        "transactiondate": str(row.transaction_date),
                        "transactiontype": str(row.transaction_type),
                        "location": str(row.location),
                        "deviceid": str(row.device_id),
                        "ipaddress": str(row.ip_address),
                        "merchantid": str(row.merchant_id),
                        "accountbalance": float(row.account_balance),
                        "previoustransactiondate": str(row.previous_transaction_date),
                        "channel": str(row.channel),
                        "customerage": int(row.customer_age),
                        "customeroccupation": str(row.customer_occupation),
                        "transactionduration": int(row.transaction_duration),
                        "loginattempts": int(row.login_attempts),
                        "status": "PENDING"
                    }
                    new_transactions.append(transaction)
            return new_transactions
        except Exception as e:
            logging.error(f"Erreur récupération transactions: {str(e)}")
            return []

    def mark_as_processed(self, transaction_id):
        try:
            self.session.execute(
                "INSERT INTO processed_transactions (transaction_id, processed_at) VALUES (%s, %s)",
                (transaction_id, datetime.now())
            )
            self.processed_transactions.add(transaction_id)
        except Exception as e:
            logging.error(f"Erreur marquage transaction: {str(e)}")

    def close(self):
        self.cluster.shutdown()
