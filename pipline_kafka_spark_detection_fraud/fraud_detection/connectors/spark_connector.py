from pyspark.sql import SparkSession
import logging

logger = logging.getLogger(__name__)

def create_spark_connection():
    """Crée et configure une session Spark"""
    try:
        spark = SparkSession.builder \
            .appName("RealTimeFraudDetection") \
            .master("local[*]") \
            .config("spark.jars.packages", 
                   "com.datastax.spark:spark-cassandra-connector_2.12:3.4.0,"
                   "org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.0") \
            .config("spark.cassandra.connection.host", "localhost") \
            .config("spark.sql.streaming.forceDeleteTempCheckpointLocation", "true") \
            .config("spark.driver.host", "localhost") \
            .config("spark.driver.bindAddress", "127.0.0.1") \
            .config("spark.network.timeout", "600s") \
            .config("spark.executor.heartbeatInterval", "300s") \
            .config("spark.ui.enabled", "false") \
            .config("spark.sql.shuffle.partitions", "1") \
            .config("spark.kafka.consumer.cache.timeout", "120000") \
            .config("spark.task.maxFailures", "10") \
            .getOrCreate()
        
        spark.sparkContext.setLogLevel("WARN")
        logger.info("Connexion Spark établie avec succès")
        return spark
    except Exception as e:
        logger.error(f"Échec de la création de la session Spark: {e}", exc_info=True)
        raise