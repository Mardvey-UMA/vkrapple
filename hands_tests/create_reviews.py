import os
import requests
from faker import Faker
import json
import random

fake = Faker("ru_RU")
BASE_URL = "http://localhost:8080/api"
IMAGES_DIR = "images"
TELEGRAM_USER_ID = "5058608908"
ARTICLES_FILE = "created_articles.json"

def load_articles():
    try:
        with open(ARTICLES_FILE, "r") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"Error: {ARTICLES_FILE} not found! Run create_products.py first.")
        exit(1)

def create_review(article_number, text, rating, photos_count):
    # Создание отзыва
    review_data = {
        "rating": rating,
        "text": text
    }

    response = requests.post(
        f"{BASE_URL}/review?article={article_number}",
        json=review_data,
        headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
    )

    if response.status_code != 200:
        print(f"Error creating review: {response.text}")
        return None

    review_id = response.json()["id"]

    # Загрузка фото для отзыва
    for i in range(photos_count):
        photo_name = f"example_review{i + 1}.jpg"
        photo_path = os.path.join(IMAGES_DIR, photo_name)

        if not os.path.exists(photo_path):
            print(f"Photo {photo_path} not found!")
            continue

        with open(photo_path, "rb") as f:
            files = {"file": (photo_name, f, "image/jpeg")}
            data = {
                "reviewId": str(review_id),
                "indexNumber": str(i)
            }

            response = requests.post(
                f"{BASE_URL}/review/photo",
                files=files,
                data=data,
                headers={"X-Telegram-User-Id": TELEGRAM_USER_ID}
            )

            if response.status_code == 200:
                print(f"Uploaded review photo {photo_name}")
            else:
                print(f"Error uploading review photo: {response.text}")

    return review_id


def main():
    # Предположим, что article_numbers - список созданных артикулов
    article_numbers = load_articles()

    for article in article_numbers:
        for _ in range(3):  # 3 отзыва на товар
            text = fake.paragraph(nb_sentences=random.randint(2, 3))
            rating = random.randint(1, 5)
            photos_count = random.randint(1, 3)

            review_id = create_review(article['article'], text, rating, photos_count)
            if review_id:
                print(f"Created review {review_id} for article {article['article']}")


if __name__ == "__main__":
    main()