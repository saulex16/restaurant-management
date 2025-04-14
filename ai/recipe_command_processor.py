import json

from langchain_google_genai import ChatGoogleGenerativeAI, GoogleGenerativeAIEmbeddings
from langchain_postgres import PGVector

import utils
from db.pgvector import engine


class RecipeCommandProcessor:

    def __init__(self):
        embeddings = GoogleGenerativeAIEmbeddings(model="models/text-embedding-004")
        self.vectorstore = PGVector(
            connection=engine,
            embeddings=embeddings,
            collection_name="recipes",
        )
        self.llm = ChatGoogleGenerativeAI(model="models/gemini-2.0-flash",
                                          response_format="json",
                                          convert_system_message_to_human=True)
        self.api_service = utils.create_restaurant_management_api_service()

    async def send_command(self, command: str):
        if command.lower() != "listar recetas posibles":
            print("Unrecognised command.")
            return

        ingredients_stock = await self.api_service.get_stocks_by_id()
        if not ingredients_stock:
            print("No stock data received.")
            return

        print(f"Stock received: {ingredients_stock}")

        query = ", ".join(ingredients_stock)
        try:
            docs = self.vectorstore.similarity_search(query=query, k=20)
        except Exception as e:
            print(f"Vector search failed: {e}")
            return

        if not docs:
            print("🔍 No matching recipes found in the vectorstore.")
            return

        recipes = []
        for doc in docs:
            metadata = doc.metadata
            recipes.append({
                "title": metadata.get("title"),
                "ingredients": metadata.get("ingredients", [])
            })

        prompt = (
            "Given the following list of available ingredients:\n"
            f"{ingredients_stock}\n\n"
            "And the following list of recipes with their required ingredients:\n"
            f"{json.dumps(recipes, indent=2)}\n\n"
            "List which recipes can be prepared with the current stock. "
            "Return only a JSON list of recipe titles like:\n"
            "[\"Garlic Bread\", \"Tomato Salad\"]\n\n"
            "ONLY the JSON. No Markdown, no explanations, no extra text."
        )

        response = self.llm.invoke(prompt)

        try:
            result = json.loads(response.content)
        except json.JSONDecodeError:
            print("Failed to parse LLM response.")
            print("Attempting fallback extraction...")
            try:
                result = utils.extract_json_from_llm_output(response.content)
            except Exception as e:
                print(f"Fallback also failed: {e}")
                print(f"Raw response:\n{response.content}")
                return []

        print("Recipes that can be prepared:")
        print(result)
        return result