from gateways.restaurant_management_api_gateway import RestaurantManagementApiGateway
from collections.abc import Callable, Awaitable
from fastapi import Response, HTTPException
from scheduler import scheduler
from datetime import datetime, timedelta, timezone
from enum import Enum
from httpx import TimeoutException, RequestError
from logger import LOG


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
        timeout = min(self.health_check_timeout_backoff, MAX_BACKOFF)
        LOG.debug("Scheduling health check in %s seconds", timeout)
        scheduler.add_job(self.execute_health_check, 'date',
                          run_date=datetime.now(timezone.utc) + timedelta(seconds=timeout))

    async def execute_health_check(self):
        path = self.health_check_path
        try:
            response = await self.gateway.get(path)
            status_code = response.status_code
            content = response.text

            if status_code == 200 and content == "Pong":
                self.health_check_timeout_backoff = None
                if self.circuit_breaker_status == CircuitBreakerStatus.HALF_OPENED:
                    self.circuit_breaker_status = CircuitBreakerStatus.CLOSED
                elif self.circuit_breaker_status == CircuitBreakerStatus.OPENED:
                    self.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
            else:
                self.circuit_breaker_status = CircuitBreakerStatus.OPENED
                self.schedule_health_check()
        except (TimeoutException, RequestError) as e:
            LOG.error("Health check failed with exception: %s", e)
            self.circuit_breaker_status = CircuitBreakerStatus.OPENED
            self.schedule_health_check()
        finally:
            LOG.debug("Health check executed with status code: %s", self.circuit_breaker_status)
            self.is_health_check_scheduled = False

    async def __handle_request(self, request: Callable[[], Awaitable[Response]]):
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

    async def get_stocks_by_id(self, restaurant_id: int):
        async def request():
            response = await self.gateway.get(f"/restaurants/{restaurant_id}/stocks")
            if not (200 <= response.status_code < 300):
                raise HTTPException(status_code=500, detail='Cannot fetch the restaurant stocks')
            json = response.json()
            return json

        return await self.__handle_request(request)
