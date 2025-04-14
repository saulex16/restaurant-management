import json
import os
import re

from services.restaurant_management_api_service import RestaurantManagementApiService


def create_restaurant_management_api_service():
    restaurant_management_api_url = os.environ['RESTAURANT_MANAGEMENT_API_URL']
    restaurant_management_api_timeout = int(os.environ['RESTAURANT_MANAGEMENT_API_TIMEOUT'])
    restaurant_management_api_health_check_path = os.environ['RESTAURANT_MANAGEMENT_API_HEALTH_CHECK_PATH']
    restaurant_management_api_health_check_timeout = int(os.environ['RESTAURANT_MANAGEMENT_API_HEALTH_CHECK_TIMEOUT'])

    return RestaurantManagementApiService(
        restaurant_management_api_url,
        restaurant_management_api_timeout,
        restaurant_management_api_health_check_path,
        restaurant_management_api_health_check_timeout
    )


def extract_json_from_llm_output(text: str):
    match = re.search(r"```json\s*(\[.*?\]|\{.*?\})\s*```", text, re.DOTALL)
    if match:
        cleaned = match.group(1)
        return json.loads(cleaned)

    match = re.search(r"(\[.*\]|\{.*\})", text, re.DOTALL)
    if match:
        return json.loads(match.group(1))

    raise ValueError("A valid JSON was not found in the LLM's response.")
