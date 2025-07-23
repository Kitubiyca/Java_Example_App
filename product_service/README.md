# Product service

Микросервис для работы с доменной сущностью продукта и типом продукта.

## WIP

Микросервис ещё не закончен. Не сделано:
 - юнит тесты;
 - интеграционные тесты;
 - javadoc
 - swagger

[Завершённый микросервис.](/user_service)

## Технологии и зависимости:

**Основное**
 - Java 17
 - Spring boot 3
 - Maven

**Spring Boot**
 - spring-boot-starter-actuator
 - spring-boot-starter-data-jpa
 - spring-boot-starter-web
 - spring-boot-starter-test
 - spring-cloud-starter-netflix-eureka-client
 - springdoc-openapi-starter-webmvc-api
 - spring-boot-starter-validation

**База данных**
 - liquibase-core
 - h2
 - postgresql

**Lombok и Mapstruct**
 - lombok
 - mapstruct
 - mapstruct-processor
 - lombok-mapstruct-binding

## Эндпоинты:

Эндпоинты (пока что без примеров запросов) описаны в swagger документации на gateway в разделе product-service:
http://localhost:8081/swagger-ui.html