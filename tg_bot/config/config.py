from pydantic_settings import BaseSettings, SettingsConfigDict
from pydantic import SecretStr
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent
class Settings(BaseSettings):
    # токен бота
    BOT_TOKEN: SecretStr

    # хост
    DB_HOST: str

    # база данных пользователи
    DB_PORT: int
    DB_USER: str
    DB_PASS: str
    DB_NAME: str
    DB_HOST_DOCKER: str

    WEBAPP_URL: str

    @property
    def DATABASE_URL_ASYNCPG_DB(self):
        return f"postgresql+asyncpg://postgres:postgres@goods_db:5432/goods_db"

    @property
    def DATABASE_URL_ASYNCPG_DOCKER_DB(self):
        return f"postgresql+asyncpg://postgres:postgres@goods_db:5432/goods_db"

    model_config = SettingsConfigDict(
        env_file=ROOT_DIR / ".env",
        env_file_encoding="utf-8"
    )

settings = Settings()
