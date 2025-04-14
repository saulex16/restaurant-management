from abc import ABC, abstractmethod

from langchain.chains.retrieval_qa.base import RetrievalQA
from langchain_community.document_loaders import PyPDFLoader
from langchain_google_genai import GoogleGenerativeAIEmbeddings, GoogleGenerativeAI
from langchain_postgres import PGVector
from langchain_text_splitters import RecursiveCharacterTextSplitter
from db.pgvector import engine
from recipe_command_processor import RecipeCommandProcessor
from recipe_uploader import RecipeUploader


class RAGService(ABC):

    @abstractmethod
    async def send_command(self, command: str):
        pass

    @abstractmethod
    def make_query(self, user_query: str):
        pass

    @abstractmethod
    def upload_document(self, document: str):
        pass


class RAGServiceImpl(RAGService):

    def __init__(self):
        self.uploader = RecipeUploader()
        self.command_processor = RecipeCommandProcessor()

    async def send_command(self, command: str):
        answer = await self.command_processor.send_command(command)
        return answer

    def make_query(self, user_query: str):
        vectorstore = self._create_vector_store()
        retriever = vectorstore.as_retriever(search_kwargs={"k": 5})

        llm = GoogleGenerativeAI(model="models/gemini-2.0-flash")

        qa_chain = RetrievalQA.from_chain_type(
            llm=llm,
            retriever=retriever,
            return_source_documents=True
        )

        result = qa_chain({"query": user_query})
        return result["result"]

    def upload_document(self, document_path: str):
        self.uploader.upload_document(document_path)

    def _create_vector_store(self):
        embeddings = GoogleGenerativeAIEmbeddings(model="models/text-embedding-004")
        vectorstore = PGVector(
            embeddings= embeddings,
            collection_name="recipes",
            connection= engine,
            use_jsonb= True
        )

        return vectorstore
    #
    # def _load_documents(self, document):
    #     loader = PyPDFLoader(document)
    #     splitter = RecursiveCharacterTextSplitter(
    #         chunk_size=300,
    #         chunk_overlap=50,
    #         length_function=len,
    #         is_separator_regex=False,
    #     )
    #     documents = loader.load_and_split(text_splitter=splitter)
    #     return documents