import httpx

class RestaurantManagementApiGateway:

    def __init__(self, url: str, timeout: int):
        self.url = url
        self.timeout = timeout

    async def get(self, path: str):
        full_url = self.url + path
        async with httpx.AsyncClient(timeout=self.timeout) as client:
            return await client.get(full_url)

    async def post(self, path: str, body: object):
        full_url = self.url + path
        async with httpx.AsyncClient(timeout=self.timeout) as client:
            return await client.post(full_url, json=body)