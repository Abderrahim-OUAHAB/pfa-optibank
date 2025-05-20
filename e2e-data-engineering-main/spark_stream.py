import json
import logging
import os
import shutil
import sys
from datetime import datetime
from typing import Any, Dict, List
import redis
import tempfile
import zipfile
import pickle
import numpy as np
import requests
from cassandra.auth import PlainTextAuthProvider
from cassandra.cluster import Cluster
from pyspark.ml import Pipeline, PipelineModel
from pyspark.ml.classification import MultilayerPerceptronClassifier
from pyspark.ml.evaluation import BinaryClassificationEvaluator
from pyspark.ml.feature import StandardScaler, StringIndexer, VectorAssembler
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, from_json, lit, udf, when
from pyspark.sql.types import (BooleanType, DoubleType, IntegerType,
                               StringType, StructField, StructType,
                               TimestampType)

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(message)s',
    handlers=[logging.StreamHandler()]
)
logger = logging.getLogger(__name__)

class FraudDetectionModel:
    """Gère le modèle de détection de fraude et son retraining"""
    def __init__(self, spark):
        self.spark = spark
        self.model = None
        self.model_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "fraud_detection_model")
        self.redis_client = redis.Redis(
            host='localhost',
            port=6379,
            db=0,
            decode_responses=False
        )
        self.model_redis_key = "latest_fraud_model"
        self.initial_training_done = False
        self.load_or_train_initial_model()
    def load_or_train_initial_model(self):
            """Charge depuis Redis ou entraîne un nouveau modèle"""
            try:
                # Essayer d'abord Redis
                if self.load_model_from_redis():
                    self.initial_training_done = True
                    logger.info("Modèle chargé depuis Redis")
                # Si Redis échoue, essayer le stockage local
                elif os.path.exists(self.model_path):
                    self.model = PipelineModel.load(self.model_path)
                    self.initial_training_done = True
                    self.save_model_to_redis()  # Synchroniser vers Redis
                    logger.info("Modèle chargé depuis le stockage local et synchronisé vers Redis")
                else:
                    logger.info("Aucun modèle existant trouvé, un nouveau sera entraîné")
                    self.initial_training_done = False
            except Exception as e:
                logger.error(f"Erreur lors du chargement du modèle: {str(e)}")
                self.initial_training_done = False
    def save_model_to_redis(self):
        """Sauvegarde le modèle actuel dans Redis"""
        try:
            if not self.model:
                return False
                
            # Création d'un fichier temporaire
            with tempfile.TemporaryDirectory() as tmp_dir:
                model_path = os.path.join(tmp_dir, "model")
                self.model.save(model_path)
                
                # Création d'une archive zip
                zip_path = os.path.join(tmp_dir, "model.zip")
                with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
                    for root, _, files in os.walk(model_path):
                        for file in files:
                            file_path = os.path.join(root, file)
                            arcname = os.path.relpath(file_path, model_path)
                            zipf.write(file_path, arcname)
                
                # Lecture de l'archive et sauvegarde dans Redis
                with open(zip_path, 'rb') as f:
                    model_data = f.read()
                
                self.redis_client.set(self.model_redis_key, model_data)
                logger.info("Modèle sauvegardé dans Redis sous forme compressée")
                return True
                
        except Exception as e:
            logger.error(f"Erreur sauvegarde Redis: {str(e)}", exc_info=True)
            return False

    def load_model_from_redis(self):
           
        try:
            model_data = self.redis_client.get(self.model_redis_key)
            if not model_data:
                return False
                
            # Création d'un répertoire temporaire
            with tempfile.TemporaryDirectory() as tmp_dir:
                zip_path = os.path.join(tmp_dir, "model.zip")
                model_path = os.path.join(tmp_dir, "model")
                
                # Écriture de l'archive
                with open(zip_path, 'wb') as f:
                    f.write(model_data)
                
                # Extraction de l'archive
                with zipfile.ZipFile(zip_path, 'r') as zipf:
                    zipf.extractall(model_path)
                
                # Chargement du modèle Spark
                self.model = PipelineModel.load(model_path)
                self.initial_training_done = True
                logger.info("Modèle chargé depuis Redis avec succès")
                return True
                
        except Exception as e:
            logger.error(f"Erreur chargement Redis: {str(e)}", exc_info=True)
            return False
    
    def reload_model(self):
        """Recharge le modèle depuis le stockage"""
        try:
            if os.path.exists(self.model_path):
                self.model = PipelineModel.load(self.model_path)
                self.initial_training_done = True
                logger.info("Modèle rechargé avec succès")
                return True
            else:
                logger.warning("Aucun modèle trouvé à recharger")
                return False
        except Exception as e:
            logger.error(f"Erreur lors du rechargement du modèle: {str(e)}")
            return False
    
    def prepare_features(self, df):
        """Prépare les caractéristiques pour l'entraînement ou la prédiction"""
        transaction_type_indexer = StringIndexer(
            inputCol="transactiontype", 
            outputCol="transactiontype_index",
            handleInvalid="keep"
        )
        location_indexer = StringIndexer(
            inputCol="location", 
            outputCol="location_index",
            handleInvalid="keep"
        )
        channel_indexer = StringIndexer(
            inputCol="channel", 
            outputCol="channel_index",
            handleInvalid="keep"
        )
        occupation_indexer = StringIndexer(
            inputCol="customeroccupation", 
            outputCol="customeroccupation_index",
            handleInvalid="keep"
        )
        
        feature_columns = [
            "transactionamount", 
            "customerage",
            "transactionduration", 
            "loginattempts",
            "accountbalance",
            "transactiontype_index",
            "location_index",
            "channel_index",
            "customeroccupation_index"
        ]
        
        assembler = VectorAssembler(inputCols=feature_columns, outputCol="features")
        scaler = StandardScaler(inputCol="features", outputCol="scaledFeatures")
        
        return Pipeline(stages=[
            transaction_type_indexer,
            location_indexer,
            channel_indexer,
            occupation_indexer,
            assembler,
            scaler
        ])
        
    def train_model(self, df):
        """Entraîne le modèle avec les nouvelles données"""
        try:
            # Création d'étiquettes simulées
            df = df.withColumn("label", 
                when((col("transactionamount") > 1000) & 
                    (col("transactionduration") < 30), 1)
                .when((col("loginattempts") > 3), 1)
                .when((col("customerage") < 25) & 
                      (col("transactionamount") > 500), 1)
                .otherwise(0)
            )
            
            pipeline = self.prepare_features(df)
            self.pipeline_model = pipeline.fit(df)
            transformed_data = self.pipeline_model.transform(df)
            
            layers = [len(self.pipeline_model.stages[-2].getInputCols()), 64, 32, 2]
            
            mlp = MultilayerPerceptronClassifier(
                layers=layers,
                featuresCol="scaledFeatures",
                labelCol="label",
                maxIter=100,
                blockSize=128,
                seed=1234
            )
            
            full_pipeline = Pipeline(stages=self.pipeline_model.stages + [mlp])
            self.model = full_pipeline.fit(df)
            
            # Sauvegarde du modèle avec création du répertoire si nécessaire
         
            
            self.initial_training_done = True
            self.save_model_to_redis() 
            logger.info(f"Modèle entraîné et sauvegardé avec succès dans redis")
            return True
        except Exception as e:
            logger.error(f"Erreur lors de l'entraînement du modèle: {str(e)}", exc_info=True)
            return False
    
    def predict(self, df):
        """Effectue des prédictions de fraude sur les données"""
        if not self.initial_training_done or not self.model:
            logger.warning("Aucun modèle disponible pour les prédictions")
            return df.withColumn("is_fraud", lit(False))
        
        try:
            predictions = self.model.transform(df)
            return predictions.withColumn("is_fraud", col("prediction") == 1)
        except Exception as e:
            logger.error(f"Erreur lors de la prédiction: {str(e)}", exc_info=True)
            return df.withColumn("is_fraud", lit(False))

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
    
    def write_transactions(self, df, is_fraud: bool) -> bool:
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

def get_transaction_schema() -> StructType:
    """Définit le schéma des transactions"""
    return StructType([
        StructField("transactionid", StringType(), False),
        StructField("accountid", StringType(), False),
        StructField("transactionamount", DoubleType(), False),
        StructField("transactiondate", TimestampType(), False),
        StructField("transactiontype", StringType(), False),
        StructField("location", StringType(), False),
        StructField("deviceid", StringType(), False),
        StructField("ipaddress", StringType(), False),
        StructField("merchantid", StringType(), False),
        StructField("accountbalance", DoubleType(), False),
        StructField("previoustransactiondate", TimestampType(), False),
        StructField("channel", StringType(), False),
        StructField("customerage", IntegerType(), False),
        StructField("customeroccupation", StringType(), False),
        StructField("transactionduration", IntegerType(), False),
        StructField("loginattempts", IntegerType(), False)
    ])

def process_stream(kafka_df, schema: StructType, cassandra_connector: CassandraConnector, fraud_model: FraudDetectionModel):
    """Traite le stream de transactions"""
    transactions_df = kafka_df \
        .selectExpr("CAST(value AS STRING)") \
        .select(from_json(col("value"), schema).alias("data")) \
        .select("data.*")
    
    last_retrain_time = datetime.now()
    batch_count = 0
    training_threshold = 100
    retrain_interval = 3600
    
    def process_batch(df, batch_id):
        nonlocal last_retrain_time, batch_count
        
        try:
            if df.rdd.isEmpty():
                logger.info("Batch vide reçu")
                return
            
            batch_count += df.count()
            current_time = datetime.now()
            
            should_retrain = (batch_count >= training_threshold) or \
                            ((current_time - last_retrain_time).total_seconds() >= retrain_interval)
            
            if should_retrain:
                logger.info("Début du retraining du modèle...")
                if fraud_model.train_model(df):
                    last_retrain_time = current_time
                    batch_count = 0
                else:
                    logger.warning("Échec du retraining, utilisation du modèle existant")
            
            predictions = fraud_model.predict(df)
            
            fraudulent = predictions.filter(col("is_fraud") == True)
            legitimate = predictions.filter(col("is_fraud") == False)
            
            cassandra_connector.write_transactions(fraudulent, is_fraud=True)
            cassandra_connector.write_transactions(legitimate, is_fraud=False)
            
            logger.info(f"Batch {batch_id} traité - Frauduleuses: {fraudulent.count()}, Légitimes: {legitimate.count()}")
            
        except Exception as e:
            logger.error(f"Erreur lors du traitement du batch {batch_id}: {str(e)}", exc_info=True)
    
    return transactions_df.writeStream \
        .foreachBatch(process_batch) \
        .option("checkpointLocation", "/tmp/checkpoint") \
        .start()

def main():
    """Point d'entrée principal"""
    KAFKA_CONFIG = {
        "bootstrap_servers": "localhost:9092",
        "topic": "bank_transactions"
    }
    
    CASSANDRA_CONFIG = {
        "keyspace": "spark_streams"
    }
    
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