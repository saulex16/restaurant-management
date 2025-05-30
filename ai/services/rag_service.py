from helpers.recipe_command_processor import RecipeCommandProcessor
from helpers.recipe_qa_processor import RecipeQuestionAnswerProcessor
from helpers.recipe_uploader import RecipeUploader
from models.commands import Commands


class RagService:

    def __init__(self):
        self.uploader = RecipeUploader()
        self.command_processor = RecipeCommandProcessor()
        self.recipe_qa_processor = RecipeQuestionAnswerProcessor()

    async def send_command(self, restaurant_id: int, command: Commands):
        answer = await self.command_processor.send_command(restaurant_id)
        return answer

    def make_query(self, user_query: str):
        return self.recipe_qa_processor.ask_question(user_query)

    def upload_document(self, document_path: str):
        self.uploader.upload_document(document_path)
