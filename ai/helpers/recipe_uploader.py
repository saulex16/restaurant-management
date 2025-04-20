import hashlib

from langchain_google_genai import ChatGoogleGenerativeAI, GoogleGenerativeAIEmbeddings
from langchain_postgres import PGVector

from langchain.docstore.document import Document
from langchain_community.document_loaders import PyPDFLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from db.pgvector import engine

import json

from logger import LOG
from models.rag.prompts import PromptManager
from utils import extract_json_from_llm_output


def generate_id(text: str) -> str:
    return hashlib.md5(text.encode()).hexdigest()


class RecipeUploader:
    def __init__(self):
        self.prompt_manager = PromptManager()

        embeddings = GoogleGenerativeAIEmbeddings(model="models/text-embedding-004")
        self.llm = ChatGoogleGenerativeAI(model="models/gemini-2.0-flash",
                                          convert_system_message_to_human=True)
        self.vectorstore = PGVector(
            connection=engine,
            embeddings=embeddings,
            collection_name="recipes",
        )
        self.splitter = RecursiveCharacterTextSplitter(
            chunk_size=5000,
            chunk_overlap=500,
            length_function=len,
            is_separator_regex=False,
        )

    def _extract_recipes_from_chunk(self, chunk: str) -> list[dict]:
        prompt = self.prompt_manager.get_extract_recipes_prompt(chunk)
        response = self.llm.invoke(prompt)

        try:
            return json.loads(response.content)
        except json.JSONDecodeError:
            LOG.error("Error parsing JSON from the LLM in this chunk.")
            LOG.error(f"{response.content}")

            try:
                return extract_json_from_llm_output(response.content)
            except Exception as e:
                LOG.error(f"Fallback also failed: {e}")
                return []

    def upload_document(self, pdf_path: str):
        loader = PyPDFLoader(pdf_path)
        pages = loader.load()
        full_text = "\n".join(page.page_content for page in pages)

        chunks = self.splitter.split_text(full_text)

        all_documents = []
        for idx, chunk in enumerate(chunks):
            LOG.debug(f"Processing chunk {idx + 1}/{len(chunks)}")
            recipes = self._extract_recipes_from_chunk(chunk)
            for rid, recipe in enumerate(recipes):
                doc = Document(
                    page_content=recipe["content"],
                    metadata={"title": recipe["title"]},
                )
                all_documents.append((doc, f"chunk{idx}_recipe{rid}"))

        LOG.debug(f"Extracted {len(all_documents)} recipes. Saving to vectorstore...")
        docs, ids = zip(*all_documents)
        self.vectorstore.add_documents(documents=list(docs), ids=list(ids))
