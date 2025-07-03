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

logger = logging.getLogger(__name__)

class FraudDetectionModel:
    """Gère le modèle de détection de fraude et son retraining"""
    def __init__(self, spark):
        self.spark = spark
        self.model = None
        self.model_path ="/Users/minfo/Desktop/PFA/e2e-data-engineering-main/fraud_detection_model" #os.path.join(os.path.dirname(os.path.abspath(__file__)), "fraud_detection_model")
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