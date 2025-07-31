# Java Example App

Микросервисное приложение на Java и Spring Boot для изучения и тестирования различных технологий и приёмов разработки.

## Технологии

 - Java 17
 - Spring boot 3
 - Maven

 - PostgreSQL 15
 - Redis 7.4
 - MongoDB 4.4

 - Kafka 4.0.0

 - Eureka 4.1.1

## Требования для запуска

 - Docker

## Сборка и локальный запуск

 1. Клонировать репозиторий:
    git clone https://github.com/Kitubiyca/Java_Example_App.git

 2. Собрать и запустить в докере:
    docker-compose up -d

## Использование

Сначала необходимо зарегистрироваться:

```
curl --location 'http://localhost:8081/sign-up/request' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "example@example.com",
  "password": "1234",
  "firstname": "Иван",
  "lastname": "Иванов",
  "patronymic": "Иванович",
  "phoneNumber": "123456789012",
  "birthDate": "1990-01-01"
}'
```

Код подтверждения можно найти в логах notification-service (или на указанной при регистрации почте при подключении smtp сервера).
При необходимости можно повторить отправку кода подтверждения:

```
curl --location 'http://localhost:8081/sign-up/resend-confirmation-code' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "example@example.com"
}'
```

Далее нужно подтвердить регистрацию. Здесь нужно указать id из ответа на первый запрос и otp из логов (или письма):
```
curl --location 'http://localhost:8081/sign-up/confirm' \
--header 'Content-Type: application/json' \
--data '{
  "id": "",
  "otp": ""
}'
```

После всех этих шагов можно войти в аккаунт как пользователь:
```
curl --location 'http://localhost:8081/sign-in/by-password' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "example@example.com",
  "password": "1234"
}'
```

Или можно использовать предсозданный аккаунт менеджера (с расширенным доступом):
```
curl --location 'http://localhost:8081/sign-in/by-password' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "example_manager@example.com",
  "password": "1234"
}'
```

Для авторизации запросов используется Bearer-токен, полученный из запроса на авторизацию пользователя. При необходимости его можно обновить, используя refresh токен.

## Документация

 - Swagger: http://localhost:8081/swagger-ui.html