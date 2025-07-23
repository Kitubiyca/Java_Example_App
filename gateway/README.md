# Gateway

Реактивный микросервис, отвечающий за маршрутизацию и авторизацию входящих запросов.

Принцип работы:
1) При запуске проверяет наличие микросервисов в eureka.
2) Собирает их OpenAPI документацию.
3) Создаёт RouteDefiniton (описание запроса) для всех запросов.
4) Сохраняет их в Redis для быстрого доступа.
5) Маршрутизирует входящие запросы на основании имеющихся RouteDefiniton.
6) Обновляет состояние приложения каждые 30 секунд.

Все правила маршрутизации, фильтрации и доступа описаны в тегах контроллеров в самих сервисах, и подтягиваются вместе со всей OpenAPI документацией.

## WIP

Микросервис ещё не закончен. Не сделано:
 - юнит тесты;
 - интеграционные тесты;
 - javadoc

[Завершённый микросервис.](/user_service)

## Технологии и зависимости:

**Основное**
 - Java 17
 - Spring boot 3
 - Maven

**Spring Boot**
 - spring-boot-starter-data-redis-reactive
 - spring-boot-starter-security
 - spring-boot-starter-webflux
 - spring-cloud-starter-gateway
 - spring-cloud-starter-netflix-eureka-client
 - springdoc-openapi-starter-webflux-ui
 - spring-cloud-starter-loadbalancer
 - spring-boot-starter-actuator

**Lombok**
 - lombok

**JSON**
 - jackson-datatype-jsr310

**JWT**
 - jjwt-api
 - jjwt-impl
 - jjwt-jackson

**Безопасность**
 - spring-security-crypto

**Тестирование**
 - spring-boot-starter-test
 - reactor-test
 - spring-security-test

**Swagger Parser**
 - swagger-parser

## Эндпоинты:

 - Swagger: http://localhost:8081/swagger-ui.html