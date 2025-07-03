from datetime import datetime
from pyspark.sql import DataFrame
from pyspark.sql.functions import col, lit
import logging
from pyspark.sql.functions import col, from_json 

logger = logging.getLogger(__name__)

def process_stream(kafka_df, schema, cassandra_connector, fraud_model):
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
                
                # Mise à jour des statuts pour les transactions frauduleuses
                for row in fraudulent.select("transactionid", "accountid").collect():
                    cassandra_connector.update_transaction_status(
                        transaction_id=row["transactionid"],
                        is_fraud=True,
                        transaction_amount=0,  # Pas de mise à jour pour les fraudes
                        transaction_type=""
                    )
                    cassandra_connector.create_alert(
                        account_id=row["accountid"],
                        message="Transaction frauduleuse détectée"
                    )
                    cassandra_connector.check_alerts_and_suspend_user(row["accountid"])
                    
                # Mise à jour des statuts et soldes pour les transactions légitimes
                for row in legitimate.select("transactionid", "accountid", "transactionamount", "transactiontype").collect():
                    cassandra_connector.update_transaction_status(
                        transaction_id=row["transactionid"],
                        is_fraud=False,
                        transaction_amount=row["transactionamount"],
                        transaction_type=row["transactiontype"]
                    )
                    
                logger.info(f"Batch {batch_id} traité - Frauduleuses: {fraudulent.count()}, Légitimes: {legitimate.count()}")
                
            except Exception as e:
                logger.error(f"Erreur lors du traitement du batch {batch_id}: {str(e)}", exc_info=True)
   
    return transactions_df.writeStream \
        .foreachBatch(process_batch) \
        .option("checkpointLocation", "/tmp/checkpoint") \
        .start()