from collections.abc import Callable
from api import app
from fastapi.testclient import TestClient

test_client = TestClient(app)

def execute_test(test: Callable, dependency_overrides: dict = None):
    if dependency_overrides:
        for key, value in dependency_overrides.items():
            app.dependency_overrides[key] = value
    try:
        test()
    finally:
        app.dependency_overrides = {}