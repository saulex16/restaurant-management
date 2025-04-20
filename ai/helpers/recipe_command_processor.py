import json

from langchain_google_genai import ChatGoogleGenerativeAI, GoogleGenerativeAIEmbeddings
from langchain_postgres import PGVector

import utils
from db.pgvector import engine
from models.rag.prompts import PromptManager
from logger import LOG


class RecipeCommandProcessor:

    def __init__(self):
        self.prompt_manager = PromptManager()

        embeddings = GoogleGenerativeAIEmbeddings(model="models/text-embedding-004")
        self.vectorstore = PGVector(
            connection=engine,
            embeddings=embeddings,
            collection_name="recipes",
        )
        self.llm = ChatGoogleGenerativeAI(model="models/gemini-2.0-flash",
                                          convert_system_message_to_human=True)
        self.api_service = utils.create_restaurant_management_api_service()

    async def send_command(self, restaurant_id: int):

        ingredients_stock = await self.api_service.get_stocks_by_id(restaurant_id)
        if not ingredients_stock:
            LOG.error("No stock data received.")
            return

        LOG.debug(f"Stock received: {ingredients_stock}")

        query = ", ".join(ingredients_stock)
        try:
            docs = self.vectorstore.similarity_search(query=query, k=20)
        except Exception as e:
            LOG.error(f"Vector search failed: {e}")
            return

        if not docs:
            LOG.error("No matching recipes found in the vectorstore.")
            return

        recipes = []
        for doc in docs:
            metadata = doc.metadata
            recipes.append({
                "title": metadata.get("title"),
                "ingredients": metadata.get("ingredients", [])
            })

        prompt = self.prompt_manager.get_recipe_recommendation_prompt(
            ingredients=ingredients_stock,
            recipes=recipes
        )
        response = self.llm.invoke(prompt)

        try:
            result = json.loads(response.content)
        except json.JSONDecodeError:
            LOG.error("Failed to parse LLM response.")
            LOG.error("Attempting fallback extraction...")
            try:
                result = utils.extract_json_from_llm_output(response.content)
            except Exception as e:
                LOG.error(f"Fallback also failed: {e}")
                LOG.error(f"Raw response:\n{response.content}")
                return []

        LOG.debug("Recipes that can be prepared:")
        LOG.debug(result)
        return result