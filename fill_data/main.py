import requests
import random

BASE_URL = 'http://localhost:8080/api/products'  # Замените на актуальный URL API

# Данные для генерации товаров
categories_data = [
    {
        "category_name": "Ручка",
        "category_id": 1,
        "attributes": {
            "Производитель": ["Parker", "Pilot", "Lamy", "Waterman", "Bic"],
            "Толщина стержня": ["0.5 мм", "0.7 мм", "1.0 мм", "0.3 мм"],
            "Модель": ["Ballpoint", "Gel", "Fountain", "Rollerball"],
            "Цвет": ["Синий", "Черный", "Красный", "Зеленый"],
            "Кол-во в упаковке": ["1", "5", "10", "12"]
        },
        "price_range": (50, 1000),
        "stock_range": (10, 200)
    },
    {
        "category_name": "Тетрадь",
        "category_id": 2,
        "attributes": {
            "Производитель": ["Moleskine", "Leuchtturm", "Brauberg", "Silwerhof", "Oxford"],
            "Линовка": ["Клетка", "Линия", "Чистая"],
            "Кол-во в упаковке": ["1", "5", "10"],
            "Кол-во страниц": ["48", "96", "120"]
        },
        "price_range": (30, 300),
        "stock_range": (20, 150)
    },
    {
        "category_name": "Ластик",
        "category_id": 3,
        "attributes": {
            "Производитель": ["Faber-Castell", "Staedtler", "Koh-I-Noor", "Pentel", "Tombow"],
            "Кол-во в упаковке": ["1", "3", "5"],
            "Твердость": ["Мягкий", "Средний", "Твердый"],
            "Тип ластика": ["Классический", "Архитектурный", "Для чернил"],
            "Цвет": ["Белый", "Серый", "Цветной"]
        },
        "price_range": (10, 150),
        "stock_range": (50, 300)
    },
    {
        "category_name": "Карандаш",
        "category_id": 4,
        "attributes": {
            "Производитель": ["Faber-Castell", "Koh-I-Noor", "Derwent", "Staedtler", "Prismacolor"],
            "Твердость": ["HB", "2B", "4H", "H"],
            "Цвет": ["Черный", "Красный", "Синий"],
            "Кол-во в упаковке": ["1", "6", "12"],
            "Ластик на конце": ["Да", "Нет"]
        },
        "price_range": (20, 200),
        "stock_range": (30, 250)
    },
    {
        "category_name": "Фломастер",
        "category_id": 5,
        "attributes": {
            "Производитель": ["Copic", "Stabilo", "ZIG", "Edding", "Touch"],
            "Кол-во в упаковке": ["6", "12", "24"],
            "Кол-во цветов": ["6", "12", "24"],
            "Тип краски": ["На спиртовой основе", "Водостойкая", "Смываемая"]
        },
        "price_range": (80, 500),
        "stock_range": (40, 180)
    }
]


def generate_products():
    for category in categories_data:
        for i in range(5):
            attributes = {}
            for attr, values in category["attributes"].items():
                attributes[attr] = values[i % len(values)]

            product_data = {
                "name": f"{category['category_name']} {i + 1}",
                "price": float(random.randint(*category["price_range"])),
                "balanceInStock": random.randint(*category["stock_range"]),
                "description": f"Тестовый {category['category_name']} ({i + 1})",
                "categoryId": category["category_id"],
                "attributes": attributes
            }

            response = requests.post(BASE_URL, json=product_data)
            print(f"Создан товар: {product_data['name']} | Статус: {response.status_code}")


if __name__ == "__main__":
    generate_products()