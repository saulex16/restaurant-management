from jinja2 import Template

import json
from typing import Dict


class PromptCache:
    _prompts: Dict[str, str] = None
    _file_path = "prompts.json"

    @classmethod
    def get_prompt(cls, key: str) -> str:
        if cls._prompts is None:
            with open(cls._file_path, "r", encoding="utf-8") as f:
                cls._prompts = json.load(f)

        if key not in cls._prompts:
            raise KeyError(f"Prompt '{key}' not found in '{cls._file_path}'")

        return cls._prompts[key]

class PromptManager:

    def __init__(self):
        self.prompt_cache = PromptCache()

    def get_extract_recipes_prompt(self, chunk: str) -> str:
        prompt_template = self.prompt_cache.get_prompt("extract_recipes")

        return Template(prompt_template).render(chunk=chunk)

    def get_recipe_recommendation_prompt(self, ingredients, recipes) -> str:
        prompt_template = self.prompt_cache.get_prompt("recipe_recommendation")

        return Template(prompt_template).render(
            ingredients=ingredients,
            recipes=json.dumps(recipes, indent=2)
        )