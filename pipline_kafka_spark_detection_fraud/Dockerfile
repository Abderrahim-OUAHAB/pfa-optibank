# Utilisez une image de base Python
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
COPY . .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 9092 9091

COPY start.sh .
RUN chmod +x start.sh

CMD ["./start.sh"]
