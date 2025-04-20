from langchain.chains.retrieval_qa.base import RetrievalQA
from langchain_google_genai import GoogleGenerativeAIEmbeddings, GoogleGenerativeAI
from langchain_postgres import PGVector

from db.pgvector import engine


class RecipeQuestionAnswerProcessor:

    def __init__(self):
        embeddings = GoogleGenerativeAIEmbeddings(model="models/text-embedding-004")

        retriever = PGVector(
            embeddings= embeddings,
            collection_name="recipes",
            connection= engine,
            use_jsonb= True
        ).as_retriever(search_kwargs={"k": 5})

        llm = GoogleGenerativeAI(model="models/gemini-2.0-flash")

        self.qa_chain = RetrievalQA.from_chain_type(
            llm=llm,
            retriever=retriever,
            return_source_documents=True
        )

    def ask_question(self, question: str):
        return self.qa_chain({"query": question})
