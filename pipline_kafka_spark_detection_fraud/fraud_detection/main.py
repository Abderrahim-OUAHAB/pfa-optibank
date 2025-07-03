import sys
import logging
from datetime import datetime
from fraud_detection.connectors.spark_connector import create_spark_connection
from fraud_detection.connectors.cassandra_connector import CassandraConnector
from fraud_detection.models.fraud_model import FraudDetectionModel
from fraud_detection.schemas.transaction_schema import get_transaction_schema
from fraud_detection.streams.kafka_stream import get_kafka_stream
from fraud_detection.streams.stream_processor import process_stream
from fraud_detection.config.settings import KAFKA_CONFIG, CASSANDRA_CONFIG

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(message)s',
    handlers=[logging.StreamHandler()]
)
logger = logging.getLogger(__name__)

def main():
    """Point d'entrée principal"""
    spark = None
    try:
        spark = create_spark_connection()
        cassandra_connector = CassandraConnector(keyspace=CASSANDRA_CONFIG["keyspace"])
        fraud_model = FraudDetectionModel(spark)
        
        # Option pour forcer le rechargement du modèle
        if "--reload" in sys.argv:
            logger.info("Forcer le rechargement du modèle...")
            if not fraud_model.reload_model():
                logger.warning("Impossible de recharger le modèle, poursuite avec le modèle existant ou nouvel entraînement")
        
        if not fraud_model.initial_training_done:
            logger.info("En attente des premières données pour l'entraînement initial...")
        
        kafka_df = get_kafka_stream(
            spark,
            KAFKA_CONFIG["bootstrap_servers"],
            KAFKA_CONFIG["topic"]
        )
        
        query = process_stream(
            kafka_df,
            get_transaction_schema(),
            cassandra_connector,
            fraud_model
        )
        
        logger.info("Démarrage du traitement du stream...")
        query.awaitTermination()
        
    except KeyboardInterrupt:
        logger.info("Arrêt demandé par l'utilisateur")
    except Exception as e:
        logger.error(f"Erreur critique: {str(e)}", exc_info=True)
    finally:
        if spark is not None:
            try:
                spark.stop()
                logger.info("Session Spark arrêtée avec succès")
            except Exception as e:
                logger.error(f"Erreur lors de l'arrêt de Spark: {str(e)}")
        logger.info("Application terminée")

if __name__ == "__main__":
    main()