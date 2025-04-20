import httpx
import pytest
from unittest.mock import patch
from gateways.restaurant_management_api_gateway import RestaurantManagementApiGateway


GATEWAY_PATH = "gateways.restaurant_management_api_gateway"
HTTPX_ASYNC_CLIENT_PREFIX = "httpx.AsyncClient"
MOCKED_GET = f"{GATEWAY_PATH}.{HTTPX_ASYNC_CLIENT_PREFIX}.get"
MOCKED_POST = f"{GATEWAY_PATH}.{HTTPX_ASYNC_CLIENT_PREFIX}.post"


@pytest.fixture(scope="module", autouse=True)
def gateway():
    return RestaurantManagementApiGateway("http://localhost:8000", 5)


@pytest.mark.anyio
@patch(MOCKED_GET)
async def test_get_successful(fake_get, gateway):
    fake_get.return_value = httpx.Response(
        status_code=200,
        text="Pong"
    )
    response = await gateway.get("/health")
    assert response.status_code == 200
    assert response.text == "Pong"


@pytest.mark.anyio
@patch(MOCKED_GET)
async def test_get_timeout(fake_get, gateway):
    fake_get.side_effect = httpx.TimeoutException('Timeout occurred')
    with pytest.raises(httpx.TimeoutException):
        await gateway.get("/health")


@pytest.mark.anyio
@patch(MOCKED_GET)
async def test_get_request_error(fake_get, gateway):
    fake_get.side_effect = httpx.RequestError('Request error occurred')
    with pytest.raises(httpx.RequestError):
        await gateway.get("/health")


@pytest.mark.anyio
@patch(MOCKED_GET)
async def test_get_internal_server_error(fake_get, gateway):
    fake_get.return_value = httpx.Response(
        status_code=500,
        text="Internal Server Error"
    )
    response = await gateway.get("/health")
    assert response.status_code == 500
    assert response.text == "Internal Server Error"


@pytest.mark.anyio
@patch(MOCKED_POST)
async def test_post_successful(fake_post, gateway):
    fake_post.return_value = httpx.Response(
        status_code=200,
        json={"message": "success"}
    )
    response = await gateway.post("/upload", {"file": "test.pdf"})
    assert response.status_code == 200
    assert response.json() == {"message": "success"}


@pytest.mark.anyio
@patch(MOCKED_POST)
async def test_post_timeout(fake_post, gateway):
    fake_post.side_effect = httpx.TimeoutException('Timeout occurred')
    with pytest.raises(httpx.TimeoutException):
        await gateway.post("/upload", {"file": "test.pdf"})


@pytest.mark.anyio
@patch(MOCKED_POST)
async def test_post_request_error(fake_post, gateway):
    fake_post.side_effect = httpx.RequestError('Request error occurred')
    with pytest.raises(httpx.RequestError):
        await gateway.post("/upload", {"file": "test.pdf"})


@pytest.mark.anyio
@patch(MOCKED_POST)
async def test_post_invalid_json(fake_post, gateway):
    fake_post.return_value = httpx.Response(
        status_code=400,
        json={"error": "Invalid JSON"}
    )
    response = await gateway.post("/upload", "invalid_json")
    assert response.status_code == 400
    assert response.json() == {"error": "Invalid JSON"}

@pytest.mark.anyio
@patch(MOCKED_POST)
async def test_post_invalid_file(fake_post, gateway):
    fake_post.return_value = httpx.Response(
        status_code=400,
        json={"error": "Invalid file"}
    )
    response = await gateway.post("/upload", {"file": "invalid_file"})
    assert response.status_code == 400
    assert response.json() == {"error": "Invalid file"}

