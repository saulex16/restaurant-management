import tempfile
from contextlib import asynccontextmanager
from fastapi import FastAPI, UploadFile, File, Depends

from models.requests import ChatRequest, CommandRequest
from services.rag_service import RagService
import utils
from scheduler import scheduler
from logger import LOG

restaurant_management_api_service = utils.create_restaurant_management_api_service()


@asynccontextmanager
async def lifespan(app: FastAPI):
    LOG.info("Starting scheduler")
    scheduler.start()
    LOG.info("Scheduler started")
    LOG.info("Executing health check")
    await restaurant_management_api_service.execute_health_check()
    LOG.info("Health check executed")
    LOG.info("Application started")
    yield
    LOG.info("Shutting down scheduler")
    scheduler.shutdown()
    LOG.info("Scheduler shut down")


app = FastAPI(lifespan=lifespan)


def get_rag_service():
    return RagService()


@app.post("/upload/")
async def upload_file(file: UploadFile = File(...), service: RagService = Depends(get_rag_service)):
    contents = await file.read()

    with tempfile.NamedTemporaryFile(delete=False, suffix=".pdf") as tmp:
        tmp.write(contents)
        tmp_path = tmp.name

    result = service.upload_document(tmp_path)
    return {"message": "Upload successful", "details": result}


@app.post("/chat/")
async def chat(request: ChatRequest, service: RagService = Depends(get_rag_service)):
    answer = service.make_query(request.message)
    return {"answer": answer}


@app.post("/command/{restaurant_id}")
async def command(request: CommandRequest, restaurant_id: int, service: RagService = Depends(get_rag_service)):
    answer = await service.send_command(restaurant_id, request.command)
    return {"answer": answer}