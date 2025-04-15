import os
import shutil
import tempfile

from contextlib import asynccontextmanager
from fastapi import FastAPI, UploadFile, File, Depends

from models.requests import ChatRequest
from services.rag_service import RAGService, RAGServiceImpl
import utils

# restaurant_management_api_service = utils.create_restaurant_management_api_service()
#
# @asynccontextmanager
# async def lifespan(app: FastAPI):
#     scheduler.start()
#     restaurant_management_api_service.execute_health_check()
#     yield
#     scheduler.shutdown()

#app = FastAPI(lifespan=lifespan)
app = FastAPI()

def get_rag_service():
    return RAGServiceImpl()

@app.post("/upload/")
async def upload_file(file: UploadFile = File(...), service: RAGService = Depends(get_rag_service)):
    contents = await file.read()

    with tempfile.NamedTemporaryFile(delete=False, suffix=".pdf") as tmp:
        tmp.write(contents)
        tmp_path = tmp.name

    result = service.upload_document(tmp_path)
    return {"message": "Upload successful", "details": result}

@app.post("/chat/")
async def chat(request: ChatRequest, service: RAGService = Depends(get_rag_service)):
    answer = service.make_query(request.message)
    return {"answer": answer}

@app.post("/command/{restaurant_id}")
async def command(restaurant_id: int, service: RAGService = Depends(get_rag_service)):
    answer = await service.send_command(restaurant_id, "listar recetas posibles")
    return {"answer": answer}