# Notification service

Микросервис для работы с внешними системами оповещения пользователей.

## Использование

По умолчанию код подтверждения пишется в логах.
Для отправки кода на почту нужно указать параметры smtp сервера (хост, порт, ссл, логин и пароль)
и mail.sender.send=true в application.properties.

## WIP

Микросервис ещё не закончен. Не сделано:
 - юнит тесты;
 - интеграционные тесты;
 - javadoc

[Завершённый микросервис.](/user_service)

## Технологии и зависимости

**Основное**
 - Java 17
 - Spring boot 3
 - Maven

**Spring Boot**
 - spring-boot-starter-actuator
 - spring-boot-starter-mail
 - 

**Lombok**
 - lombok

**Кафка**
 - spring-kafka

**JSON**
 - jackson-core
 - jackson-databind

**Валидация**
 - jakarta.validation-api