from gateways.restaurant_management_api_gateway import RestaurantManagementApiGateway
from collections.abc import Callable, Awaitable
from fastapi import Response, HTTPException
from api import scheduler
from datetime import datetime, timedelta, timezone
from enum import Enum
from httpx import TimeoutException, RequestError

class CircuitBreakerStatus(Enum):
    CLOSED = 1
    OPENED = 2
    HALF_OPENED = 3

MAX_BACKOFF = 300

class RestaurantManagementApiService:

    def __init__(self, api_url: str, api_timeout: int, health_check_path: str, health_check_timeout: int):
        self.gateway = RestaurantManagementApiGateway(api_url, api_timeout)
        self.health_check_path = health_check_path
        self.health_check_timeout_init = health_check_timeout
        self.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
        self.is_health_check_scheduled = False
        self.health_check_timeout_backoff = None

    def schedule_health_check(self):
        if self.is_health_check_scheduled:
            return
        self.is_health_check_scheduled = True
        if self.health_check_timeout_backoff is not None:
            self.health_check_timeout_backoff = self.health_check_timeout_backoff * 2
        else:
            self.health_check_timeout_backoff = self.health_check_timeout_init
        timeout = max(self.health_check_timeout_backoff, MAX_BACKOFF)
        scheduler.add_job(self.execute_health_check, 'date',
                          run_date=datetime.now(timezone.utc) + timedelta(seconds=timeout))

    async def execute_health_check(self):
        path = self.health_check_path
        try:
            response = await self.gateway.get(path)
            status_code = response.status_code
            if 200 <= status_code < 300:
                self.health_check_timeout_backoff = None
                if self.circuit_breaker_status == CircuitBreakerStatus.HALF_OPENED:
                    self.circuit_breaker_status = CircuitBreakerStatus.CLOSED
                elif self.circuit_breaker_status == CircuitBreakerStatus.OPENED:
                    self.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
            else:
                self.circuit_breaker_status = CircuitBreakerStatus.OPENED
                self.schedule_health_check()
        except (TimeoutException, RequestError):
            self.circuit_breaker_status = CircuitBreakerStatus.OPENED
            self.schedule_health_check()
        finally:
            self.is_health_check_scheduled = False


    async def handle_request(self, request: Callable[[], Awaitable[Response]]):
        exception = HTTPException(status_code=500, detail='Cannot connect to Restaurant Management API')
        if self.circuit_breaker_status == CircuitBreakerStatus.OPENED:
            raise exception
        try:
            response = await request()
            self.circuit_breaker_status = CircuitBreakerStatus.CLOSED
            return response
        except (TimeoutException, RequestError):
            self.schedule_health_check()
            self.circuit_breaker_status = CircuitBreakerStatus.OPENED
            raise exception


    async def add_recept(self, recept_name: str, recept_ingredients: list[str]):
        # TODO: Chequear con la api de java los endpoint y que datos necesitan
        async def request():
            ingredient_ids = []
            for ingredient in recept_ingredients:
                response = await self.gateway.post("/ingredients", {'name': ingredient})
                json = response.json()
                ingredient_id = json['id']
                ingredient_ids.append(ingredient_id)
            await self.gateway.post("/recepts", {'name': recept_name, 'ingredients': ingredient_ids})
        return await self.handle_request(request)
