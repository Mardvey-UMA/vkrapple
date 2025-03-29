import os
import requests
import random
import json

BASE_URL = "http://localhost:8080/api"
IMAGES_DIR = "images"
TELEGRAM_USER_ID = "5058608908"
ARTICLES_FILE = "created_articles.json"

# Соответствие категорий их ID (предположение)
CATEGORIES = {
    "Ручка": 1,
    "Блокнот": 2,
    "Ластик": 3,
    "Стикер": 4
}

import random

products = [
    # Ручки
    {
        "name": "Ручка черная СГУ",
        "category": "Ручка",
        "price": 150.00,
        "balance": random.randint(100, 300),
        "description": "Черная ручка с логотипом СГУ",
        "attributes": {
            "Производитель": "СГУ",
            "Толщина стержня": "0.5 мм",
            "Коллекция": "СГУ - выбор лучших",
            "Цвет": "Черный",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen1.jpg"]
    },
    {
        "name": "Набор ручек СГУ 3 шт.",
        "category": "Ручка",
        "price": 400.00,
        "balance": random.randint(100, 300),
        "description": "Набор из трех ручек разных цветов",
        "attributes": {
            "Производитель": "СГУ",
            "Толщина стержня": "0.7 мм",
            "Коллекция": "СГУ - выбор лучших",
            "Цвет": "Разноцветный",
            "Кол-во в упаковке": "3"
        },
        "photos": ["pen2.jpg"]
    },
    {
        "name": "Ручка синяя СГУ",
        "category": "Ручка",
        "price": 150.00,
        "balance": random.randint(100, 300),
        "description": "Синяя ручка с логотипом СГУ",
        "attributes": {
            "Производитель": "СГУ",
            "Толщина стержня": "0.5 мм",
            "Коллекция": "СГУ - выбор лучших",
            "Цвет": "Синий",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen1.jpg"]
    },

    # Стикеры
    {
        "name": "Стикерпарк Пейзажи СГУ",
        "category": "Стикер",
        "price": 250.00,
        "balance": random.randint(100, 300),
        "description": "Набор стикеров с пейзажами",
        "attributes": {
            "Производитель": "СГУ",
            "Коллекция": "Юбилей СГУ",
            "Кол-во в наборе": "4"
        },
        "photos": ["stickerpack1.jpg"]
    },
    {
        "name": "Стикер парк легенда СГУ",
        "category": "Стикер",
        "price": 300.00,
        "balance": random.randint(100, 300),
        "description": "Коллекционные стикеры с историческими зданиями",
        "attributes": {
            "Производитель": "СГУ",
            "Коллекция": "Воспоминания СГУ",
            "Кол-во в наборе": "4"
        },
        "photos": ["stickerpack2.jpg"]
    },
    {
        "name": "Полный стикерпарк СГУ",
        "category": "Стикер",
        "price": 500.00,
        "balance": random.randint(100, 300),
        "description": "Полная коллекция стикеров",
        "attributes": {
            "Производитель": "СГУ",
            "Коллекция": "Юбилей СГУ",
            "Кол-во в наборе": "8"
        },
        "photos": ["stcikerpack1.jpg", "stickerpack2.jpg"]
    },

    # Ластики
    {
        "name": "Ластик СГУ",
        "category": "Ластик",
        "price": 80.00,
        "balance": random.randint(100, 300),
        "description": "Ластик с логотипом университета",
        "attributes": {
            "Производитель": "СГУ",
            "Коллекция": "Юбилей СГУ",
            "Твердость": "Мягкий"
        },
        "photos": ["erase1.jpg"]
    },

    # Блокноты
    {
        "name": "Блокнот синий СГУ",
        "category": "Блокнот",
        "price": 450.00,
        "balance": random.randint(100, 300),
        "description": "Синий блокнот в клетку",
        "attributes": {
            "Производитель": "СГУ",
            "Линовка": "Клетка",
            "Кол-во страниц": "96",
            "Коллекция": "Деловой СГУ",
            "Цвет": "Синий"
        },
        "photos": ["notebook1.jpg"]
    },
    {
        "name": "Блокнот белый СГУ",
        "category": "Блокнот",
        "price": 450.00,
        "balance": random.randint(100, 300),
        "description": "Белый блокнот в клетку",
        "attributes": {
            "Производитель": "СГУ",
            "Линовка": "Клетка",
            "Кол-во страниц": "96",
            "Коллекция": "Деловой СГУ",
            "Цвет": "Белый"
        },
        "photos": ["notebook2.jpg"]
    },
    {
        "name": "Блокнот синий СГУ",
        "category": "Блокнот",
        "price": 450.00,
        "balance": random.randint(100, 300),
        "description": "Синий блокнот в линейку",
        "attributes": {
            "Производитель": "СГУ",
            "Линовка": "Линия",
            "Кол-во страниц": "96",
            "Коллекция": "Деловой СГУ",
            "Цвет": "Синий"
        },
        "photos": ["notebook1.jpg"]
    },
    {
        "name": "Блокнот белый СГУ",
        "category": "Блокнот",
        "price": 450.00,
        "balance": random.randint(100, 300),
        "description": "Белый блокнот в линейку",
        "attributes": {
            "Производитель": "СГУ",
            "Линовка": "Линия",
            "Кол-во страниц": "96",
            "Коллекция": "Деловой СГУ",
            "Цвет": "Белый"
        },
        "photos": ["notebook2.jpg"]
    }
]


def create_product(product_data):
    category_id = CATEGORIES[product_data["category"]]

    payload = {
        "name": product_data["name"],
        "price": product_data["price"],
        "balanceInStock": product_data["balance"],
        "description": product_data["description"],
        "categoryId": category_id,
        "attributes": product_data["attributes"]
    }

    response = requests.post(f"{BASE_URL}/products", json=payload)
    if response.status_code != 200:
        print(f"Error creating product: {response.text}")
        return None
    print(response.json())
    article_number = int(response.json())
    print(f"Created product: {product_data['name']} (Article: {article_number})")
    return article_number


def upload_photos(article_number, photos):
    for idx, photo in enumerate(photos):
        file_path = os.path.join(IMAGES_DIR, photo)
        if not os.path.exists(file_path):
            print(f"Photo {file_path} not found!")
            continue

        with open(file_path, "rb") as f:
            files = {"file": (photo, f, "image/jpeg")}
            data = {
                "articleNumber": str(article_number),
                "indexNumber": str(idx)
            }

            response = requests.post(
                f"{BASE_URL}/products/photo",
                files=files,
                data=data
            )

            if response.status_code == 200:
                print(f"Uploaded photo {photo} for article {article_number}")
            else:
                print(f"Error uploading photo: {response.text}")


def main():
    created_articles = []

    for product in products:
        article = create_product(product)
        if article:
            upload_photos(article, product["photos"])
            created_articles.append({
                "name": product["name"],
                "article": article,
                "category": product["category"],
                "category_id":CATEGORIES[product["category"]] ,
            })

    # Сохраняем артикулы в файл
    with open(ARTICLES_FILE, "w", encoding="utf-8") as f:
        json.dump(created_articles, f, indent=2, ensure_ascii=False)

    print("Created articles saved to", ARTICLES_FILE)


if __name__ == "__main__":
    main()