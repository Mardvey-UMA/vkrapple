# test_search.py
import requests
import json
import os
from datetime import datetime
from urllib.parse import urlencode

BASE_URL = "http://localhost:8080/api"
OUTPUT_DIR = "test_search"
ARTICLES_FILE = "created_articles.json"


def save_test_case(test_name, response, request_info=None):
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')

    # Сохраняем информацию о запросе
    if request_info:
        with open(os.path.join(OUTPUT_DIR, f"{timestamp}_{test_name}_request.txt"), "w", encoding="utf-8") as f:
            f.write(f"URL: {request_info['url']}\n")
            f.write(f"Method: {request_info['method']}\n")
            f.write(f"Params: {json.dumps(request_info['params'], indent=2, ensure_ascii=False)}\n")
            f.write(f"Headers: {json.dumps(dict(request_info['headers']), indent=2, ensure_ascii=False)}\n")

    # Сохраняем ответ
    try:
        response_data = response.json()
    except json.JSONDecodeError:
        response_data = {
            "error": "Invalid JSON response",
            "status_code": response.status_code,
            "content": response.text
        }

    with open(os.path.join(OUTPUT_DIR, f"{timestamp}_{test_name}_response.json"), "w", encoding="utf-8") as f:
        json.dump(response_data, f, indent=2, ensure_ascii=False)


def load_articles():
    try:
        with open(ARTICLES_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"Error: {ARTICLES_FILE} not found! Run create_products.py first.")
        exit(1)


def get_category_id(category_name, articles):
    for item in articles:
        if item["category"] == category_name:
            return item["category_id"]  # Предполагаем, что category_id сохранен
    return None


def test_search_flow():
    articles = load_articles()

    # Тестовые случаи
    test_cases = [
        {
            "name": "blocknot_linovka",
            "category": "Блокнот",
            "params": {
                "attributes[Линовка]": ["Клетка", "Линия"]
            }
        },
        {
            "name": "stickers_collection",
            "category": "Стикер",
            "params": {
                "attributes[Коллекция]": ["Юбилей СГУ", "Воспоминания СГУ"]
            }
        },
        {
            "name": "pen_color",
            "category": "Ручка",
            "params": {
                "attributes[Цвет]": ["Черный", "Синий"]
            }
        },
        {
            "name": "combined_filters",
            "category": "Блокнот",
            "params": {
                "attributes[Линовка]": ["Клетка"],
                "attributes[Цвет]": ["Синий"]
            }
        },
        {
            "name": "invalid_attribute",
            "category": "Ручка",
            "params": {
                "attributes[Несуществующий_атрибут]": ["Значение"]
            }
        }
    ]

    for case in test_cases:
        category_id = get_category_id(case["category"], articles)
        if not category_id:
            print(f"Category {case['category']} not found")
            continue

        params = [("categoryId", str(category_id))]
        for attr, values in case["params"].items():
            for value in values:
                params.append((f"attributes[{attr}]", value))

        request_url = f"{BASE_URL}/products/search?{urlencode(params)}"

        response = requests.get(
            f"{BASE_URL}/products/search",
            params=params
        )

        save_test_case(
            case["name"],
            response,
            request_info={
                "url": request_url,
                "method": "GET",
                "params": params,
                "headers": response.request.headers
            }
        )


def test_price_filter():
    # Тестирование фильтрации по цене
    params = [
        ("minPrice", "100"),
        ("maxPrice", "300")
    ]

    response = requests.get(
        f"{BASE_URL}/products/search",
        params=params
    )

    save_test_case(
        "price_filter",
        response,
        request_info={
            "url": f"{BASE_URL}/products/search?{urlencode(params)}",
            "method": "GET",
            "params": params,
            "headers": response.request.headers
        }
    )


def test_full_text_search():
    # Тестирование полнотекстового поиска
    params = [
        ("query", "СГУ")
    ]

    response = requests.get(
        f"{BASE_URL}/products/search",
        params=params
    )

    save_test_case(
        "fulltext_search",
        response,
        request_info={
            "url": f"{BASE_URL}/products/search?{urlencode(params)}",
            "method": "GET",
            "params": params,
            "headers": response.request.headers
        }
    )


if __name__ == "__main__":
    test_search_flow()
    test_price_filter()
    test_full_text_search()