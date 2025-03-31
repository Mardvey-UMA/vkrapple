import requests
import json
import os
from datetime import datetime, timedelta
import time

BASE_URL = "http://localhost:8080/api"
TELEGRAM_USER_ID = "5058608908"
OUTPUT_DIR = "test_cart_orders"
ARTICLES_FILE = "created_articles.json"


def save_response(test_name, response):
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    filename = f"{datetime.now().strftime('%H%M%S')}_{test_name}.json"

    with open(os.path.join(OUTPUT_DIR, filename), "w") as f:
        json.dump(response.json(), f, indent=2, ensure_ascii=False)


def load_articles():
    try:
        with open(ARTICLES_FILE, "r") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"Error: {ARTICLES_FILE} not found! Run create_products.py first.")
        exit(1)


def test_complex_cart_flow(articles):
    # Выбираем 3 разных товара для тестов
    test_articles = [
        articles[0],  # Ручка
        articles[2],  # Ластик
        articles[5]  # Блокнот
    ]

    # Тест 1: Добавление первого товара
    print("Testing 01_add_first_item")
    add_data = {"articleNumber": test_articles[0]["article"], "quantity": 2}
    response = requests.post(
        f"{BASE_URL}/cart/add",
        json=add_data,
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )

    save_response("01_add_first_item", response)
    time.sleep(1)
    # Тест 2: Проверка корзины с одним товаром
    print("Testing 02_cart_with_one_item")
    response = requests.get(
        f"{BASE_URL}/cart/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )

    save_response("02_cart_with_one_item", response)
    time.sleep(1)
    print("Testing 03_add_second_item")
    # Тест 3: Добавление второго товара
    add_data = {"articleNumber": test_articles[1]["article"], "quantity": 1}
    response = requests.post(
        f"{BASE_URL}/cart/add",
        json=add_data,
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("03_add_second_item", response)
    time.sleep(1)
    print("Testing 04_cart_with_two_items")
    # Тест 4: Проверка корзины с двумя товарами
    response = requests.get(
        f"{BASE_URL}/cart/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("04_cart_with_two_items", response)
    time.sleep(1)
    print("Testing 05_update_first_item")
    # Тест 5: Обновление количества первого товара
    add_data = {"articleNumber": test_articles[0]["article"], "quantity": 3}
    response = requests.post(
        f"{BASE_URL}/cart/add",
        json=add_data,
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("05_update_first_item", response)
    time.sleep(1)
    print("Testing 06_add_third_item")
    # Тест 6: Добавление третьего товара
    add_data = {"articleNumber": test_articles[2]["article"], "quantity": 2}
    response = requests.post(
        f"{BASE_URL}/cart/add",
        json=add_data,
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("06_add_third_item", response)
    time.sleep(1)
    print("Testing 07_cart_with_three_items")
    # Тест 7: Проверка корзины с тремя товарами
    response = requests.get(
        f"{BASE_URL}/cart/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("07_cart_with_three_items", response)
    time.sleep(1)
    print("Testing 08_remove_second_item")
    # Тест 8: Удаление второго товара
    response = requests.delete(
        f"{BASE_URL}/cart/remove?article={test_articles[1]['article']}",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("08_remove_second_item", response)
    time.sleep(1)
    print("Testing 09_cart_after_removal")
    # Тест 9: Проверка корзины после удаления
    response = requests.get(
        f"{BASE_URL}/cart/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("09_cart_after_removal", response)
    time.sleep(1)
    print("Testing 10_create_order")
    # Тест 10: Создание заказа
    order_data = {
        "paymentMethod": "CARD",
        "orderAddress": "ул. Пушкина, 10"
    }
    response = requests.post(
        f"{BASE_URL}/orders/create",
        json=order_data,
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("10_create_order", response)
    time.sleep(1)
    print(response.json())
    order_id = response.json()["id"]
    print("Testing 11_orders_list")
    # Тест 11: Проверка списка заказов
    response = requests.get(
        f"{BASE_URL}/orders/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("11_orders_list", response)
    time.sleep(1)
    print("Testing 12_cancel_order")
    # Тест 12: Отмена заказа
    response = requests.delete(
        f"{BASE_URL}/orders/cancel?orderId={order_id}",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("12_cancel_order", response)
    time.sleep(1)
    print("Testing 13_final_cart_state")
    # Тест 13: Проверка корзины после заказа
    response = requests.get(
        f"{BASE_URL}/cart/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("13_final_cart_state", response)
    print("Testing 14_orders_list")
    # Тест 11: Проверка списка заказов
    response = requests.get(
        f"{BASE_URL}/orders/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("14_orders_list", response)
if __name__ == "__main__":
    articles_data = load_articles()

    if len(articles_data) < 6:
        print("Need at least 6 products for full test")
        exit(1)

    test_complex_cart_flow(articles_data)