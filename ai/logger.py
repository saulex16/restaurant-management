import logging
import os

LOG = logging.getLogger('uvicorn.error')
log_level_str = os.getenv("LOG_LEVEL", "INFO").upper()
numeric_level = getattr(logging, log_level_str, logging.INFO)
LOG.setLevel(numeric_level)
