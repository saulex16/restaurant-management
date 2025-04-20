import httpx
import json
from services.restaurant_management_api_service import RestaurantManagementApiService, CircuitBreakerStatus
import pytest
from unittest.mock import patch
from fastapi import HTTPException

GATEWAY_PATH = 'gateways.restaurant_management_api_gateway.RestaurantManagementApiGateway'
SCHEDULER_PATH = 'scheduler.scheduler'


@pytest.fixture(scope="function")
def service(request):
    timeout = getattr(request, "param", 5)
    return RestaurantManagementApiService(
        api_url="http://localhost:8000",
        api_timeout=timeout,
        health_check_path="/health",
        health_check_timeout=timeout,
    )


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_execute_health_check(mock_add_job, mock_get, service):
    mock_get.return_value = httpx.Response(
        status_code=200,
        text="Pong"
    )
    await service.execute_health_check()
    assert service.circuit_breaker_status == CircuitBreakerStatus.CLOSED
    assert service.health_check_timeout_backoff is None
    assert mock_get.called
    assert not mock_add_job.called


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_execute_health_check_timeout(mock_add_job, mock_get, service):
    mock_get.side_effect = httpx.TimeoutException('Timeout occurred')
    await service.execute_health_check()
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert service.health_check_timeout_backoff is not None
    assert mock_get.called
    assert mock_add_job.called


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_execute_health_check_request_error(mock_add_job, mock_get, service):
    mock_get.side_effect = httpx.RequestError('Request error occurred')
    await service.execute_health_check()
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert service.health_check_timeout_backoff is not None
    assert mock_get.called
    assert mock_add_job.called


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_execute_health_check_internal_server_error(mock_add_job, mock_get, service):
    mock_get.return_value = httpx.Response(
        status_code=500,
        text="Internal Server Error"
    )
    await service.execute_health_check()
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert service.health_check_timeout_backoff is not None
    assert mock_get.called
    assert mock_add_job.called


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_closed_success(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.CLOSED
    mock_get.return_value = httpx.Response(
        status_code=200,
        content=json.dumps({"stocks": []}),
        headers={"Content-Type": "application/json"}
    )
    response = await service.get_stocks_by_id(1)
    assert response == {"stocks": []}
    assert service.circuit_breaker_status == CircuitBreakerStatus.CLOSED
    assert not mock_add_job.called


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_opened(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.OPENED
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert not mock_get.called
    assert not mock_add_job.called


@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_half_opened_success(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
    mock_get.return_value = httpx.Response(
        status_code=200,
        content=json.dumps({"stocks": []}),
        headers={"Content-Type": "application/json"}
    )
    response = await service.get_stocks_by_id(1)
    assert response == {"stocks": []}
    assert service.circuit_breaker_status == CircuitBreakerStatus.CLOSED
    assert not mock_add_job.called

@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_half_opened_failure(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
    mock_get.return_value = httpx.Response(
        status_code=500,
        content="Internal Server Error"
    )
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert service.circuit_breaker_status == CircuitBreakerStatus.HALF_OPENED
    assert not mock_add_job.called

@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_half_opened_timeout(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
    mock_get.side_effect = httpx.TimeoutException('Timeout occurred')
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert mock_add_job.called

@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_half_opened_request_error(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.HALF_OPENED
    mock_get.side_effect = httpx.RequestError('Request error occurred')
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert mock_add_job.called

@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_closed_internal_server_error(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.CLOSED
    mock_get.return_value = httpx.Response(
        status_code=500,
        content="Internal Server Error"
    )
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert service.circuit_breaker_status == CircuitBreakerStatus.CLOSED
    assert not mock_add_job.called

@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_closed_timeout(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.CLOSED
    mock_get.side_effect = httpx.TimeoutException('Timeout occurred')
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert mock_add_job.called

@pytest.mark.anyio
@patch(GATEWAY_PATH + '.get')
@patch(SCHEDULER_PATH + '.add_job')
async def test_get_stocks_by_id_cb_closed_request_error(mock_add_job, mock_get, service):
    service.circuit_breaker_status = CircuitBreakerStatus.CLOSED
    mock_get.side_effect = httpx.RequestError('Request error occurred')
    with pytest.raises(HTTPException):
        await service.get_stocks_by_id(1)
    assert service.circuit_breaker_status == CircuitBreakerStatus.OPENED
    assert mock_add_job.called
