#!/bin/bash

# Lancer kafka_stream/main.py en arrière-plan
python kafka_stream/main.py &

# Lancer fraud_detection/main.py
python fraud_detection/main.py
