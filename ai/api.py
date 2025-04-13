import os
import shutil
import tempfile

from contextlib import asynccontextmanager
from fastapi import FastAPI, UploadFile, File, Depends

from models.requests import ChatRequest
from services.rag_service import RAGService, RAGServiceImpl
from services.restaurant_management_api_service import RestaurantManagementApiService
from scheduler import scheduler

# restaurant_management_api_url = os.environ['RESTAURANT_MANAGEMENT_API_URL']
# restaurant_management_api_timeout = int(os.environ['RESTAURANT_MANAGEMENT_API_TIMEOUT'])
# restaurant_management_api_health_check_path = os.environ['RESTAURANT_MANAGEMENT_API_HEALTH_CHECK_PATH']
# restaurant_management_api_health_check_timeout = int(os.environ['RESTAURANT_MANAGEMENT_API_HEALTH_CHECK_TIMEOUT'])
#
# restaurant_management_api_service = RestaurantManagementApiService(
#     restaurant_management_api_url,
#     restaurant_management_api_timeout,
#     restaurant_management_api_health_check_path,
#     restaurant_management_api_health_check_timeout
# )
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

@app.post("/command/")
async def command(message: str, service: RAGService = Depends(get_rag_service)):
    answer = service.send_command(message)
    return {"answer": answer}