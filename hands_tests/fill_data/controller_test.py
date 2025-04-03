import requests

BASE_URL = 'http://localhost:8080/api/products/search'

test_cases = [
    # Ручки
    {
        'description': '1. Ручки Parker с толщиной 0.5 мм',
        'params': {
            'categoryId': 1,
            #'attributes[1]': 'Parker',
            'attributes[2]': '0.5 мм',
            'attributes[2]': '0.7 мм'
        }
    },
    {
        'description': '2. Синие ручки в упаковке по 1 шт',
        'params': {
            'categoryId': 1,
            'attributes[4]': 'Синий',  # Цвет (attributeId=4)
            'attributes[5]': '1'  # Кол-во в упаковке (attributeId=5)
        }
    },

    # Тетради
    {
        'description': '3. Тетради Moleskine в клетку 48 стр',
        'params': {
            'categoryId': 2,
            'attributes[6]': 'Moleskine',  # Производитель (attributeId=6)
            'attributes[7]': 'Клетка',  # Линовка (attributeId=7)
            'attributes[9]': '48'  # Кол-во страниц (attributeId=9)
        }
    },
    {
        'description': '4. Тетради Oxford с линовкой',
        'params': {
            'categoryId': 2,
            'attributes[6]': 'Oxford',  # Производитель (attributeId=6)
            'attributes[7]': 'Линия'  # Линовка (attributeId=7)
        }
    },

    # Ластики
    {
        'description': '5. Белые ластики Faber-Castell',
        'params': {
            'categoryId': 3,
            'attributes[10]': 'Faber-Castell',  # Производитель (attributeId=10)
            'attributes[14]': 'Белый'  # Цвет (attributeId=14)
        }
    },
    {
        'description': '6. Ластики для чернил',
        'params': {
            'categoryId': 3,
            'attributes[13]': 'Для чернил'  # Тип ластика (attributeId=13)
        }
    },

    # Карандаши
    {
        'description': '7. Карандаши HB с ластиком',
        'params': {
            'categoryId': 4,
            'attributes[16]': 'HB',  # Твердость (attributeId=16)
            'attributes[19]': 'Да'  # Ластик на конце (attributeId=19)
        }
    },
    {
        'description': '8. Карандаши Staedtler в упаковке 1 шт',
        'params': {
            'categoryId': 4,
            'attributes[15]': 'Staedtler',  # Производитель (attributeId=15)
            'attributes[18]': '1'  # Кол-во в упаковке (attributeId=18)
        }
    },

    # Общие тесты
    {
        'description': '9. Все товары категории Тетрадь',
        'params': {
            'categoryId': 2
        }
    },
    {
        'description': '10. Некорректный запрос (несуществующий атрибут)',
        'params': {
            'categoryId': 1,
            'attributes[999]': 'Test'
        }
    }
]


def test_search_endpoint():
    for case in test_cases:
        print(f"\nТест: {case['description']}")

        try:
            response = requests.get(BASE_URL, params=case['params'])
            response.raise_for_status()

            data = response.json()
            print(f"URL: {response.url}")
            print(f"Найдено товаров: {data['totalProducts']}")
            print(f"Статус код: {response.status_code}")

            if data['totalProducts'] > 0:
                print("Примеры товаров:")
                for product in data['products']:
                    print(product)
            else:
                print("Товары не найдены")

        except requests.exceptions.RequestException as e:
            print(f"Ошибка запроса: {str(e)}")


if __name__ == "__main__":
    #test_search_endpoint()
    r = requests.get("http://localhost:8080/api/products/search?categoryId=1&attributes%5B2%5D=0.7+%D0%BC%D0%BC&attributes%5B2%5D=0.5+%D0%BC%D0%BC")
    print(r.json())
    #test_search_endpoint()
