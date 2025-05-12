import csv
import json
import logging
import time
import requests
from kafka import KafkaProducer
# Configurer les logs
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(message)s',
    handlers=[logging.StreamHandler()]
)


def get_bank_transactions(file_path):
    with open(file_path, mode='r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            yield row

def format_data(transaction):
    return {
        "transactionid": transaction["TransactionID"],
        "accountid": transaction["AccountID"],
        "transactionamount": float(transaction["TransactionAmount"]),
        "transactiondate": transaction["TransactionDate"],
        "transactiontype": transaction["TransactionType"],
        "location": transaction["Location"],
        "deviceid": transaction["DeviceID"],
        "ipaddress": transaction["IP Address"],
        "merchantid": transaction["MerchantID"],
        "accountbalance": float(transaction["AccountBalance"]),
        "previoustransactiondate": transaction["PreviousTransactionDate"],
        "channel": transaction["Channel"],
        "customerage": int(transaction["CustomerAge"]),
        "customeroccupation": transaction["CustomerOccupation"],
        "transactionduration": int(transaction["TransactionDuration"]),
        "loginattempts": int(transaction["LoginAttempts"]),
    }

def stream_data():
    producer = KafkaProducer(
        bootstrap_servers='localhost:9092',
        request_timeout_ms=10000, 
        metadata_max_age_ms=600000  
    )


    file_path = "dataset/bank_transactions.csv"

    while True:

        for transaction in get_bank_transactions(file_path):
            try:
                formatted_data = format_data(transaction)
                logging.info(f"Sending: {formatted_data}")
                producer.send('bank_transactions', json.dumps(formatted_data).encode('utf-8'))
                time.sleep(1)
            except Exception as e:
                logging.error(f"Error processing transaction: {e}")
                continue

        logging.info("All transactions streamed. Sleeping for 1 hour....")
        time.sleep(3600)  

if __name__ == "__main__":
    stream_data()
