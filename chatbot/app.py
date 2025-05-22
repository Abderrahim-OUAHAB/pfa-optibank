import csv
import logging
from flask import Flask, json, request, jsonify
from pinecone_utils import init_pinecone, load_data_from_json
from groq_utils import get_llm
from langchain.chains import ConversationalRetrievalChain
from langchain.memory import ConversationBufferWindowMemory
from langchain.prompts import PromptTemplate
from flask_cors import CORS
import os
import requests

app = Flask(__name__)
CORS(app, origins=["http://localhost:4200"])
CSV_PATH = "../e2e-data-engineering-main/dataset/bank_transactions.csv"
# Configuration du logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Prompt
PROMPT_TEMPLATE = PROMPT_TEMPLATE = """Vous êtes BANBot, un assistant spécialisé pour les clients bancaires. Votre mission est de fournir des réponses claires et précises sur les services bancaires.

Contexte:
{context}

Question:
{question}

Règles strictes:
1. Répondez de manière professionnelle et courtoise
2. Fournissez des informations exactes sur les produits/services bancaires
3. Pour les urgences (carte bloquée, fraude...), indiquez immédiatement le numéro dédié
4. Pour les localisations, proposez toujours l'agence la plus proche
5. Structurez bien vos réponses avec des paragraphes clairs
6. Donnez les numéros de contact appropriés
7. Si la question concerne un problème technique, orientez vers le service client
8. Pour les questions complexes, proposez un rendez-vous en agence

Exemples de réponses:
- Pour une carte perdue: "Veuillez immédiatement bloquer votre carte au 0 892 705 705"
- Pour un virement: "Vous pouvez effectuer un virement via votre application mobile ou en agence"
- Pour un prêt: "Nos conseillers peuvent vous proposer différentes solutions de financement"

Objectif :
Fournir une assistance bancaire de qualité en toutes circonstances.
"""

prompt = PromptTemplate(
    template=PROMPT_TEMPLATE,
    input_variables=["context", "question"]
)

llm = get_llm()
vectorstore = init_pinecone()
memory = ConversationBufferWindowMemory(
    k=1,
    memory_key="chat_history",
    return_messages=True,
    output_key='answer'
)

qa_chain = ConversationalRetrievalChain.from_llm(
    llm=llm,
    retriever=vectorstore.as_retriever(search_kwargs={"k": 1}),
    memory=memory,
    combine_docs_chain_kwargs={"prompt": prompt},
    return_source_documents=True,
    output_key='answer',
    verbose=True
)

# Fonction Geoapify
def find_nearest_bank(lat, lon):
    api_key = os.getenv("GEOAPIFY_API_KEY") or "1eaa80850f9f47e28a45b23f50424cd2"
    url = f"https://api.geoapify.com/v2/places?categories=service.financial&filter=circle:{lon},{lat},5000&bias=proximity:{lon},{lat}&limit=1&apiKey={api_key}"
    
    try:
        response = requests.get(url, timeout=5)
        response.raise_for_status()
        data = response.json()

        if not data.get("features"):
            return "\n\nAucune agence bancaire trouvée dans un rayon de 5 km."

        feature = data["features"][0]
        props = feature.get("properties", {})
        
        bank_info = {
            "name": props.get("name", "Agence bancaire"),
            "address": props.get("formatted", "Adresse non disponible"),
            "phone": props.get("contact", {}).get("phone"),
            "opening_hours": props.get("opening_hours", {}).get("text"),
            "distance": props.get("distance", 0),  # en mètres
            "coordinates": feature.get("geometry", {}).get("coordinates", [])
        }

        # Construction du message
        result = "\n\nAGENCE BANCAIRE LA PLUS PROCHE :"
        result += f"\n🏦 {bank_info['name']}"
        result += f"\n📍 {bank_info['address']}"
        
        if bank_info['phone']:
            result += f"\n📞 {bank_info['phone']}"
            
        if bank_info['opening_hours']:
            result += f"\n🕒 Horaires: {bank_info['opening_hours']}"
            
        if bank_info['coordinates']:
            lon, lat = bank_info['coordinates']
            maps_link = f"https://www.google.com/maps/search/?api=1&query={lat},{lon}"
            result += f"\n🗺️ Voir sur la carte: {maps_link}"
            
        result += f"\n📏 Distance: {bank_info['distance']} mètres"
        
        result += "\n\nℹ️ En cas d'urgence (carte perdue/volée): 0 892 705 705 (24h/24)"

        return result

    except requests.exceptions.RequestException as e:
        print(f"Erreur API Geoapify: {str(e)}")
        return "\n\nService de localisation temporairement indisponible"
    except Exception as e:
        print(f"Erreur inattendue: {str(e)}")
        return "\n\nErreur lors de la recherche d'agences bancaires"

@app.route('/init', methods=['POST'])
def initialize_data():
    try:
        load_data_from_json()
        return jsonify({"status": "success", "message": "Données chargées avec succès"})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500
    
    
@app.route('/ask', methods=['POST'])
def ask():
    data = request.get_json()
    if not data:
        return jsonify({"error": "Données JSON manquantes", "status": "error"}), 400

    user_input = data.get('message', '')
    if not user_input:
        return jsonify({"error": "Aucun message fourni", "status": "error"}), 400

    try:
        # Appel à la chaîne de conversation
        result = qa_chain.invoke({"question": user_input})
        
        # Conversion sécurisée de la réponse
        answer = ""
        if isinstance(result.get("answer"), dict):
            answer = json.dumps(result["answer"])  # Convertit le dict en string JSON
        else:
            answer = str(result.get("answer", "Aucune réponse générée"))

        # Gestion de la localisation
        location = data.get('location', {})
        if location and isinstance(location, dict):
            try:
                lat = location.get("latitude")
                lon = location.get("longitude")
                if lat is not None and lon is not None:
                    lat = float(lat)
                    lon = float(lon)
                    if lat != 0 and lon != 0:
                        # Dans la fonction ask(), remplacez hospital_info par:
                        bank_info = find_nearest_bank(lat, lon)
                        if bank_info:
                            answer += str(bank_info)
            except (TypeError, ValueError) as e:
                print(f"Erreur de conversion de coordonnées: {str(e)}")

        # Préparation des sources
        sources = []
        seen = set()
        for doc in result.get("source_documents", []):
            try:
                content = str(doc.page_content) if hasattr(doc, 'page_content') else "Contenu non disponible"
                metadata = doc.metadata if hasattr(doc, 'metadata') else {}
                key = hash(content + str(metadata))
                if key not in seen:
                    seen.add(key)
                    sources.append({
                        "page_content": content,
                        "metadata": metadata
                    })
            except Exception as e:
                print(f"Erreur traitement document: {str(e)}")
                continue

        return jsonify({
            "answer": answer,
            "sources": sources,
            "status": "success"
        })

    except Exception as e:
        print(f"Erreur lors du traitement: {str(e)}", flush=True)
        return jsonify({
            "error": "Erreur interne du serveur",
            "status": "error",
            "details": str(e)
        }), 500


    
@app.route('/reset', methods=['POST'])
def reset_conversation():
    try:
        # Réinitialisation complète comme un redémarrage
        global llm, vectorstore, memory, qa_chain
        
        # 1. Réinitialiser les composants LangChain
        llm = get_llm()  # Recréer une nouvelle instance LLM
        vectorstore = init_pinecone()  # Recharger le vectorstore
        
        # 2. Recréer la mémoire avec un nouvel ID de session
        memory = ConversationBufferWindowMemory(
            memory_key="chat_history",
            return_messages=True,
            output_key='answer',
            k=1
        )
        
        # 3. Recréer la chaîne de conversation
        qa_chain = ConversationalRetrievalChain.from_llm(
            llm=llm,
            retriever=vectorstore.as_retriever(search_kwargs={"k": 3}),
            memory=memory,
            combine_docs_chain_kwargs={"prompt": prompt},
            return_source_documents=True,
            output_key='answer',
            verbose=False
        )
        
        # 4. Nettoyer les variables d'état
        # (Ajoutez ici d'autres variables globales à réinitialiser)
        
        return jsonify({
            "status": "success",
            "message": "Conversation complètement réinitialisée",
            "new_session_id": str(id(memory))  # Identifiant unique pour debug
        })
        
    except Exception as e:
        print(f"Erreur lors de la réinitialisation: {str(e)}", flush=True)
        return jsonify({
            "status": "error",
            "message": f"Échec de la réinitialisation: {str(e)}"
        }), 500





if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
