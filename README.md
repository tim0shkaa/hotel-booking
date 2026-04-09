# Hotel Booking API

REST API для управления бронированием номеров в отеле. Реализует полный цикл работы с гостями, номерами и бронями, включая публикацию событий в Kafka и метрики через Micrometer.

## Технологии

- **Kotlin** + **Spring Boot 3.3**
- **PostgreSQL 17** — хранение данных
- **JdbcTemplate / NamedParameterJdbcTemplate** — работа с БД без ORM
- **Liquibase** — миграции схемы
- **Apache Kafka** — публикация событий при изменении сущностей
- **Micrometer / Prometheus** — метрики приложения
- **Gradle 8.6**
- **Docker Compose** — локальный запуск PostgreSQL и Kafka

## Запуск

**Требования:** Git, Docker, Docker Compose, JDK 21

```shell
# Поднять PostgreSQL и Kafka
docker compose up -d

# Запустить приложение
./gradlew bootRun
```

Приложение стартует на `http://localhost:8080`.

## API

### Гости `/api/guests`

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/{id}` | Получить гостя по ID |
| `POST` | `/` | Создать гостя |
| `PUT` | `/{id}` | Обновить данные гостя |

### Номера `/api/rooms`

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/{id}` | Получить номер по ID |
| `POST` | `/` | Создать номер |
| `PUT` | `/{id}` | Обновить номер |
| `DELETE` | `/{id}` | Удалить номер |

### Брони `/api/bookings`

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/{id}` | Получить бронь по ID |
| `GET` | `/available` | Поиск свободных номеров на дату и время |
| `POST` | `/` | Создать бронь |
| `PUT` | `/{id}` | Обновить бронь |
| `DELETE` | `/{id}` | Отменить бронь |

## Kafka

При создании и изменении сущностей публикуются события в соответствующие топики:

- `guests` — события по гостям
- `rooms` — события по номерам
- `bookings` — события по бронированию

## Метрики

Доступны через Spring Actuator на `/actuator`. Кастомные метрики (Micrometer):

- `guests.created` — количество созданных гостей
- `bookings.created` — количество созданных броней
- `errors.not.found` — количество 404-ошибок

## Структура проекта

```
src/main/kotlin/.../hotelbooking/
├── controller/     # REST-контроллеры
├── service/        # Бизнес-логика
├── dao/            # Слой доступа к данным (JdbcTemplate)
├── dto/            # Request / Response объекты
├── entity/         # Сущности
├── kafka/          # Producer и события
├── metrics/        # Кастомные метрики
└── exception/      # Глобальная обработка ошибок
```

Миграции БД: `src/main/resources/liquibase/`