import httpx
from logger import LOG


class RestaurantManagementApiGateway:

    def __init__(self, url: str, timeout: int):
        self.url = url
        self.timeout = timeout

    async def get(self, path: str):
        full_url = self.url + path
        LOG.debug("Making GET request to %s", full_url)
        async with httpx.AsyncClient(timeout=self.timeout) as client:
            return await client.get(full_url)

    async def post(self, path: str, body: object):
        full_url = self.url + path
        LOG.debug("Making POST request to %s with body %s", full_url, body)
        async with httpx.AsyncClient(timeout=self.timeout) as client:
            return await client.post(full_url, json=body)
