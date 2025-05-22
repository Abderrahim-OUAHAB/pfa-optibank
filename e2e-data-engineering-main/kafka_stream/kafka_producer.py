import json
from kafka import KafkaProducer

def create_kafka_producer():
    return KafkaProducer(
        bootstrap_servers='localhost:9092',
        value_serializer=lambda v: json.dumps(v).encode('utf-8'),
        acks='all',
        retries=3
    )
