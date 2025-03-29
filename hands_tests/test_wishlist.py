import requests
import json
import os

BASE_URL = "http://localhost:8080/api"
TELEGRAM_USER_ID = "5058608908"
OUTPUT_DIR = "test_wishlist"
ARTICLES_FILE = "created_articles.json"

def save_response(test_name, response):
    os.makedirs(os.path.join(OUTPUT_DIR, test_name), exist_ok=True)

    with open(os.path.join(OUTPUT_DIR, test_name, "response.json"), "w") as f:
        json.dump(response.json(), f, indent=2, ensure_ascii=False)

def load_articles():
    try:
        with open(ARTICLES_FILE, "r") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"Error: {ARTICLES_FILE} not found! Run create_products.py first.")
        exit(1)


def test_wishlist_flow(articles_data):
    # Добавление в список желаний
    article1 = articles_data[0]["article"]
    article2 = articles_data[1]["article"]

    response = requests.post(
        f"{BASE_URL}/wishlist/add?article={article1}",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("add_to_wishlist1", response)

    # Просмотр списка
    response = requests.get(
        f"{BASE_URL}/wishlist/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("view_wishlist2", response)

    response = requests.post(
        f"{BASE_URL}/wishlist/add?article={article2}",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("add_to_wishlist3", response)

    # Просмотр списка
    response = requests.get(
        f"{BASE_URL}/wishlist/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("view_wishlist4", response)


    # Удаление из списка
    response = requests.delete(
        f"{BASE_URL}/wishlist/remove?article={article2}",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("remove_from_wishlist5", response)

    # Просмотр списка
    response = requests.get(
        f"{BASE_URL}/wishlist/list",
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )
    save_response("view_wishlist6", response)

if __name__ == "__main__":
    articles_data = load_articles()
    test_wishlist_flow(articles_data)