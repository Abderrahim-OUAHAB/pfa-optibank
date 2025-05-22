import time
import logging
from kafka_stream.cassandra_reader import CassandraStreamReader
from kafka_stream.kafka_producer import create_kafka_producer

def stream_data():
    producer = None
    cassandra_reader = None

    try:
        producer = create_kafka_producer()
        cassandra_reader = CassandraStreamReader()
        logging.info("Streaming démarré")

        while True:
            transactions = cassandra_reader.get_new_transactions()

            if not transactions:
                logging.debug("Aucune transaction. Pause 5s.")
                time.sleep(5)
                continue

            for transaction in transactions:
                try:
                    producer.send('bank_transactions', transaction)
                    cassandra_reader.mark_as_processed(transaction['transactionid'])
                    logging.info(f"Envoyée: {transaction['transactionid']}")
                except Exception as e:
                    logging.error(f"Erreur: {transaction['transactionid']}: {str(e)}")

            time.sleep(1)

    except KeyboardInterrupt:
        logging.info("Arrêt demandé par l'utilisateur")
    except Exception as e:
        logging.error(f"Erreur critique: {str(e)}")
    finally:
        if producer:
            producer.flush()
            producer.close()
        if cassandra_reader:
            cassandra_reader.close()
        logging.info("Streaming arrêté")
