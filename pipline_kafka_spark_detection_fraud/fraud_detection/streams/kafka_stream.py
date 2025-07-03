from pyspark.sql import SparkSession
import logging

logger = logging.getLogger(__name__)

def get_kafka_stream(spark: SparkSession, bootstrap_servers: str, topic: str):
    """Crée un stream Kafka"""
    try:
        logger.info(f"Connexion à Kafka: {bootstrap_servers}, topic: {topic}")
        return spark.readStream \
            .format("kafka") \
            .option("kafka.bootstrap.servers", bootstrap_servers) \
            .option("subscribe", topic) \
            .option("startingOffsets", "earliest") \
            .option("failOnDataLoss", "false") \
            .option("maxOffsetsPerTrigger", 1000) \
            .load()
    except Exception as e:
        logger.error(f"Échec de création du stream Kafka: {str(e)}", exc_info=True)
        raise