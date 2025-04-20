from pydantic import BaseModel

from models.commands import Commands


class ChatRequest(BaseModel):
    message: str

class CommandRequest(BaseModel):
    command: Commands