
# Banking Transactions Monitoring System

## Prérequis

Installe les outils suivants sur ta machine :

- Node.js (v18+)
- Angular CLI (`npm install -g @angular/cli`)
- Python 3.10+
- Java 17+
- Maven
- Redis
- Cassandra
- Apache Kafka
- Zookeeper

---

## 1. Lancer le frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

---

## 2. Lancer le backend (Spring Boot)

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

---

## 3. Lancer le chatbot (Flask)

Crée un environnement virtuel Python :

```bash
cd chatbot
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python3 app.py
```

---

## 4. Lancer Redis

```bash
redis-server
# Ouvrir un autre terminal :
redis-cli
```

---

## 5. Lancer Cassandra

```bash
cassandra
# Ouvrir un autre terminal :
cqlsh
```

---

## 6. Lancer Zookeeper

```bash
cd kafka
bin/zookeeper-server-start.sh config/zookeeper.properties
```

---

## 7. Lancer Kafka

```bash
cd kafka
bin/kafka-server-start.sh config/server.properties
```

---

## 8. Lancer le module Kafka Stream (Python)

```bash
cd kafka_stream
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python3 -m kafka_stream.main
```

---

## 9. Lancer le module Fraud Detection (Python)

```bash
cd fraud_detection
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python3 -m fraud_detection.main
```

---

## Structure du projet

- `frontend/` : Application Angular (UI)
- `backend/` : API REST Spring Boot
- `chatbot/` : Assistant conversationnel basé sur Flask
- `kafka_stream/` : Consommateur Kafka pour les transactions
- `fraud_detection/` : Analyseur de fraude temps réel
- `kafka/` : Kafka + Zookeeper config

---

## Remarques

- Utilise plusieurs terminaux pour chaque module
- Cassandra stocke les transactions
- Kafka gère le flux de données en temps réel
