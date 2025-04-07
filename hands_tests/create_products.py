import os
import sys
import time
from io import BytesIO

import requests
import random
import json

BASE_URL = "http://localhost:8080/api"
IMAGES_DIR = "images"
TELEGRAM_USER_ID = "5058608908"
ARTICLES_FILE = "created_articles.json"

# Соответствие категорий их ID (предположение)
CATEGORIES = {
                "Тетрадь" : 1,
                "Ежедневник" : 2,
                "Ластик" : 3,
                "Ланъярд" : 4,
                "Блокнот": 5,
                "Ручка" : 6,
                "Значок" : 7,
                "Набор" : 8
}

products = [
{
        "name": "Тетрадь в клетку 12 листов",
        "category": "Тетрадь",
        "price": 50.00,
        "balance": random.randint(100, 300),
        "number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Тетрадь в клетку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "12",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Клетка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["copybook-cage1.png", "copybook-cage2.png","copybook-cage3.png"]
    },
{
        "name": "Тетрадь в клетку 24 листа",
        "category": "Тетрадь",
        "price": 70.00,
        "balance": random.randint(100, 300),
        "number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Тетрадь в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "24",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Клетка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["copybook-cage1.png", "copybook-cage2.png","copybook-cage3.png"]
    },
{
        "name": "Тетрадь в клетку 48 листов",
        "category": "Тетрадь",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Тетрадь в клетку из 48 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "48",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Клетка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["copybook-cage1.png", "copybook-cage2.png","copybook-cage3.png"]
    },
{
        "name": "Тетрадь в линейку 12 листов",
        "category": "Тетрадь",
        "price": 50.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Тетрадь в линейку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "12",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["copybook-ruler1.png", "copybook-ruler2.png","copybook-ruler3.png"]
    },
{
        "name": "Тетрадь в линейку 24 листа",
        "category": "Тетрадь",
        "price": 70.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Тетрадь в линейку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "24",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["copybook-ruler1.png", "copybook-ruler2.png","copybook-ruler3.png"]
    },
{
        "name": "Тетрадь в линейку 48 листов",
        "category": "Тетрадь",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Тетрадь в линейку из 48 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "48",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["copybook-ruler1.png", "copybook-ruler2.png","copybook-ruler3.png"]
    },
{
        "name": "Набор тетрадей в клетку 12 листов",
        "category": "Тетрадь",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор тетрадей в клетку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "12",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Клетка",
            "Кол-во в упаковке": "3"
        },
        "photos": ["copybook-cage1.png", "copybook-cage2.png","copybook-cage3.png"]
    },
{
        "name": "Набор тетрадей в клетку 24 листа",
        "category": "Тетрадь",
        "price": 180.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор тетрадей в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "24",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Клетка",
            "Кол-во в упаковке": "3"
        },
        "photos": ["copybook-cage1.png", "copybook-cage2.png","copybook-cage3.png"]
    },
{
        "name": "Набор тетрадей в клетку 48 листов",
        "category": "Тетрадь",
        "price": 270.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор тетрадей в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "48",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Клетка",
            "Кол-во в упаковке": "3"
        },
        "photos": ["copybook-cage1.png", "copybook-cage2.png","copybook-cage3.png"]
    },
{
        "name": "Набор тетрадей в линейку 12 листов",
        "category": "Тетрадь",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор тетрадей в клетку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "12",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "3"
        },
        "photos": ["copybook-ruler1.png", "copybook-ruler2.png","copybook-ruler3.png"]
    },
{
        "name": "Набор тетрадей в линейку 24 листа",
        "category": "Тетрадь",
        "price": 180.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор тетрадей в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "24",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "3"
        },
        "photos": ["copybook-ruler1.png", "copybook-ruler2.png","copybook-ruler3.png"]
    },
{
        "name": "Набор тетрадей в линейку 48 листов",
        "category": "Тетрадь",
        "price": 270.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор тетрадей в клетку из 48 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
        "attributes": {
            "Производитель": "СГУ",
            "Количество листов": "48",
            "Коллекция": "СГУ для студентов",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "3"
        },
        "photos": ["copybook-ruler1.png", "copybook-ruler2.png","copybook-ruler3.png"]
    },
{
        "name": "Ежедневник синий",
        "category": "Ежедневник",
        "price": 400.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ежедневник с логотипом СГУ и надписью на корешке в синей расцветке с резинкой",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Синий",
            "Коллекция": "СГУ для студентов",
            " Количество листов ": "128",
            "Кол-во в упаковке": "1"
        },
        "photos": ["datebook-blue.png"]
    },
{
        "name": "Ежедневник белый",
        "category": "Ежедневник",
        "price": 400.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ежедневник с логотипом СГУ и надписью на корешке в белой расцветке с резинкой",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Белый",
            "Коллекция": "СГУ для студентов",
            " Количество листов ": "128",
            "Кол-во в упаковке": "1"
        },
        "photos": ["datebook-white.png"]
    },
{
        "name": "Набор ежедневников",
        "category": "Ежедневник",
        "price": 1000.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ежедневники с логотипом СГУ и надписью на корешке в белой и синей расцветке с резинкой",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Белый и синий",
            "Коллекция": "СГУ для студентов",
            " Количество листов ": "128",
            "Кол-во в упаковке": "3"
        },
        "photos": ["datebook-set.png"]
    },
{
        "name": "Ластик мягкий",
        "category": "Ластик",
        "price": 50.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Мягкий ластик, подходит для работы с тонкой бумагой и простым карандашом",
        "attributes": {
            "Производитель": "СГУ",
            "Тип": "Мягкий",
            "Коллекция": "СГУ для студентов",
            "Цвет": "Белый",
            "Кол-во в упаковке": "1"
        },
        "photos": ["eraser.png"]
    },
{
        "name": "Ластик твердый",
        "category": "Ластик",
        "price": 50.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Твердый ластик, подходит для работы с плотной бумагой",
        "attributes": {
            "Производитель": "СГУ",
            "Тип": "Твердый",
            "Коллекция": "СГУ для студентов",
            "Цвет": "Белый",
            "Кол-во в упаковке": "1"
        },
        "photos": ["eraser.png"]
    },
{
        "name": "Ластик для стирания чернил",
        "category": "Ластик",
        "price": 70.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ластик подходит для стирания любых видов чернил, рекомендуется применять только на плотной бумаге",
        "attributes": {
            "Производитель": "СГУ",
            "Тип": "Для чернил",
            "Коллекция": "СГУ для студентов",
            "Цвет": "Белый",
            "Кол-во в упаковке": "1"
        },
        "photos": ["eraser.png"]
    },
{
        "name": "Набор мягких ластиков",
        "category": "Ластик",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Мягкие ластики, подходят для работы с тонкой бумагой и простым карандашом",
        "attributes": {
            "Производитель": "СГУ",
            "Тип": "Мягкий",
            "Коллекция": "СГУ для студентов",
            "Цвет": "Разноцветные",
            "Кол-во в упаковке": "3"
        },
        "photos": ["erasers.png"]
    },
{
        "name": "Набор твердых ластиков",
        "category": "Ластик",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Твердые ластики, подходят для работы с плотной бумагой",
        "attributes": {
            "Производитель": "СГУ",
            "Тип": "Твердый",
            "Коллекция": "СГУ для студентов",
            "Цвет": "Разноцветные",
            "Кол-во в упаковке": "3"
        },
        "photos": ["erasers.png"]
    },
{
        "name": "Ланъярд розовый",
        "category": "Ланъярд",
        "price": 70.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ланъярд для бейджей и пропускных карт, в розовом цвете",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Розовый",
            "Коллекция": "СГУ – наш выбор",
            "Материал": "Репс",
            "Кол-во в упаковке": "1"
        },
        "photos": ["lanyard-pink.png"]
    },
{
        "name": "Ланъярд синий",
        "category": "Ланъярд",
        "price": 70.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ланъярд для бейджей и пропускных карт, в синем цвете",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Синий",
            "Коллекция": "СГУ – наш выбор",
            "Материал": "Репс",
            "Кол-во в упаковке": "1"
        },
        "photos": ["lanyard-blue.png"]
    },
{
        "name": "Блокнот А6 60 листов",
        "category": "Блокнот",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Блокнот для записей с логотипом СГУ, формат А6, 60 листов",
        "attributes": {
            "Производитель": "СГУ",
            "Формат": "А6",
            "Коллекция": "СГУ – наш выбор",
            "Количество листов": "60",
            "Кол-во в упаковке": "1"
        },
        "photos": ["notebook1.png","notebook2.png"]
    },
{
        "name": "Блокнот А6 100 листов",
        "category": "Блокнот",
        "price": 150.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Блокнот для записей с логотипом СГУ, формат А6, 100 листов",
        "attributes": {
            "Производитель": "СГУ",
            "Формат": "А6",
            "Коллекция": "СГУ – наш выбор",
            "Количество листов": "100",
            "Кол-во в упаковке": "1"
        },
        "photos": ["notebook1.png","notebook2.png"]
    },
{
        "name": "Блокнот А5 40 листов",
        "category": "Блокнот",
        "price": 70.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Блокнот для записей с логотипом СГУ, формат А5, 40 листов",
        "attributes": {
            "Производитель": "СГУ",
            "Формат": "А5",
            "Коллекция": "СГУ – наш выбор",
            "Количество листов": "40",
            "Кол-во в упаковке": "1"
        },
        "photos": ["notepad1.png","notepad2.png"]
    },
{
        "name": "Блокнот А5 80 листов",
        "category": "Блокнот",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Блокнот для записей с логотипом СГУ, формат А5, 80 листов",
        "attributes": {
            "Производитель": "СГУ",
            "Формат": "А5",
            "Коллекция": "СГУ – наш выбор",
            "Количество листов": "80",
            "Кол-во в упаковке": "1"
        },
        "photos": ["notepad1.png","notepad2.png"]
    },
{
        "name": "Блокнот А5 80 листов",
        "category": "Блокнот",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Блокнот для записей с логотипом СГУ, формат А5, 80 листов",
        "attributes": {
            "Производитель": "СГУ",
            "Формат": "А5",
            "Коллекция": "СГУ – наш выбор",
            "Количество листов": "80",
            "Кол-во в упаковке": "1"
        },
        "photos": ["notepad1.png","notepad2.png"]
    },
{
        "name": "Ручка бирюзовая, синие чернила",
        "category": "Ручка",
        "price": 40.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ручка бирюзово-синего цвета с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Синий",
            "Цвет ручки": "Бирюзовый",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen-blue.png"]
    },
{
        "name": "Ручка фиолетовая, синие чернила",
        "category": "Ручка",
        "price": 40.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ручка фиолетово-синего цвета с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Синий",
            "Цвет ручки": "Фиолетовый",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen-violet.png"]
    },
{
        "name": "Ручка желтая, синие чернила",
        "category": "Ручка",
        "price": 40.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ручка желто-синего цвета с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Синий",
            "Цвет ручки": "Желтый",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen-yellow.png"]
    },
{
        "name": "Набор ручек, синие чернила",
        "category": "Ручка",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор разноцветных ручек с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Синий",
            "Цвет ручки": "Разноцветные",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "3"
        },
        "photos": ["pen-set.png"]
    },
{
        "name": "Ручка бирюзовая, черные чернила",
        "category": "Ручка",
        "price": 40.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ручка бирюзово-синего цвета с черными чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Черный",
            "Цвет ручки": "Бирюзовый",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen-blue.png"]
    },
{
        "name": "Ручка фиолетовая, черные чернила",
        "category": "Ручка",
        "price": 40.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ручка фиолетово-синего цвета с черными чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Черный",
            "Цвет ручки": "Фиолетовый",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen-violet.png"]
    },
{
        "name": "Ручка желтая, черные чернила",
        "category": "Ручка",
        "price": 40.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ручка желто-синего цвета с черными чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Черный",
            "Цвет ручки": "Желтый",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pen-yellow.png"]
    },
{
        "name": "Набор ручек, черные чернила",
        "category": "Ручка",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор разноцветных ручек с черными чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Черный",
            "Цвет ручки": "Разноцветные",
            "Коллекция": "СГУ – наш выбор",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "3"
        },
        "photos": ["pen-set.png"]
    },
{
        "name": "Набор ручек «Юбилей СГУ», черные чернила",
        "category": "Ручка",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор разноцветных ручек из коллекции «Юбилей СГУ» с черными чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Черный",
            "Цвет ручки": "Разноцветные",
            "Коллекция": "Юбилей СГУ",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "3"
        },
        "photos": ["pen-another.png"]
    },
{
        "name": "Набор ручек «Юбилей СГУ», синие чернила",
        "category": "Ручка",
        "price": 120.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Набор разноцветных ручек из коллекции «Юбилей СГУ» с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет чернил": "Синий",
            "Цвет ручки": "Разноцветные",
            "Коллекция": "Юбилей СГУ",
            "Толщина стержня": "0.5мм",
            "Кол-во в упаковке": "3"
        },
        "photos": ["pen-another.png"]
    },
{
        "name": "Значок синий",
        "category": "Значок",
        "price": 50.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Значок синего цвета с логотипом СГУ",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Синий",
            "Коллекция": "Юбилей СГУ",
            "Вид замка": "булавка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pin-blue.png"]
    },
{
        "name": "Значок белый",
        "category": "Значок",
        "price": 50.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Значок белого цвета с логотипом СГУ",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Белый",
            "Коллекция": "Юбилей СГУ",
            "Вид замка": "булавка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["pin-white.png"]
    },
{
        "name": "Набор значков",
        "category": "Значок",
        "price": 170.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Значки синего и белого цвета с логотипом СГУ",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Белый и синий",
            "Коллекция": "Юбилей СГУ",
            "Вид замка": "булавка",
            "Кол-во в упаковке": "4"
        },
        "photos": ["pin-set.png"]
    },
{
        "name": "Набор ежедневник + ручка, синий цвет",
        "category": "Набор",
        "price": 450.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ежедневник синего цвета с логотипом СГУ и ручка с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Синий",
            "Коллекция": "СГУ для студентов",
            "Количество листов": "128",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["set-blue.png"]
    },
{
        "name": "Набор ежедневник + ручка, белый цвет",
        "category": "Набор",
        "price": 450.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": "Ежедневник белого цвета с логотипом СГУ и ручка с синими чернилами",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Белый",
            "Коллекция": "СГУ для студентов",
            "Количество листов": "128",
            "Разметка": "Линейка",
            "Кол-во в упаковке": "1"
        },
        "photos": ["set-white.png"]
    },
{
        "name": "Набор стикеров «Виды СГУ»",
        "category": "Набор",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": " Набор стикеров «Виды СГУ» из 4-х стикеров",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Разноцветные",
            "Коллекция": "Юбилей СГУ",
            "Кол-во в упаковке": "4"
        },
        "photos": ["stickers-version1.png"]
    },
{
        "name": "Набор стикеров «Наш СГУ»",
        "category": "Набор",
        "price": 100.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": " Набор стикеров «Наш СГУ» из 4-х стикеров",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Разноцветные",
            "Коллекция": "СГУ – наш выбор",
            "Кол-во в упаковке": "4"
        },
        "photos": ["stickers-version2.png"]
    },
{
        "name": "Набор стикеров «Любимый СГУ»",
        "category": "Набор",
        "price": 100.00,
        "balance": random.randint(100, 300),
        "number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": " Набор стикеров «Любимый СГУ» из 4-х стикеров",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Разноцветные",
            "Коллекция": "СГУ – наш выбор",
            "Кол-во в упаковке": "4"
        },
        "photos": ["stickers-version3.png"]
    },
{
        "name": "Полный набор стикеров",
        "category": "Набор",
        "price": 180.00,
        "balance": random.randint(100, 300),
"number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5)),
        "description": " Набор стикеров, посвященных СГУ, 8 штук",
        "attributes": {
            "Производитель": "СГУ",
            "Цвет": "Разноцветные",
            "Коллекция": "СГУ для студентов",
            "Кол-во в упаковке": "8"
        },
        "photos": ["stickers-set.png"]
    }
]
def create_product(product_data):
    category_id = CATEGORIES[product_data["category"]]
    payload = {
        "name": product_data["name"],
        "price": product_data["price"],
        "balance_in_stock": product_data["balance"],
        "description": product_data["description"],
        "category_id": category_id,
        "attributes": product_data["attributes"],
        "number_of_orders": random.randint(100, 300),
        "rating": float(random.randint(4, 5))
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
        print(file_path)
        if not os.path.exists(file_path):
            print(f"Photo {file_path} not found!")
            continue

        with open(file_path, "rb") as f:
            files = {"file": (photo, f, "image/png")}
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
            time.sleep(3)

    # Сохраняем артикулы в файл
    with open(ARTICLES_FILE, "w", encoding="utf-8") as f:
        json.dump(created_articles, f, indent=2, ensure_ascii=False)

    print("Created articles saved to", ARTICLES_FILE)


if __name__ == "__main__":
    main()