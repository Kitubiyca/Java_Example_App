# User service

Микросервис для работы с доменной сущностью пользователя, регистрации, авторизации и других взаимодествий с пользовательским аккаунтом.

## Технологии и зависимости

**Основное**
 - Java 17
 - Spring boot 3
 - Maven

**Spring Boot**
 - spring-boot-starter-actuator
 - spring-boot-starter-data-jpa
 - spring-boot-starter-data-redis
 - spring-boot-starter-web
 - spring-boot-starter-test
 - spring-cloud-starter-netflix-eureka-client
 - springdoc-openapi-starter-webmvc-api
 - spring-boot-starter-data-mongodb
 - spring-cloud-starter-openfeign
 - spring-cloud-starter-loadbalancer
 - spring-boot-starter-validation
 - spring-boot-configuration-processor

**Postgres**
 - liquibase-core
 - postgresql

**Lombok и Mapstruct**
 - lombok
 - mapstruct
 - mapstruct-processor
 - lombok-mapstruct-binding

**Кафка**
 - spring-kafka

**JSON**
 - jackson-core

**JWT**
 - jjwt-api
 - jjwt-impl
 - jjwt-jackson

**Безопасность**
 - spring-security-crypto

**Тестирование**
 - spring-kafka-test
 - spring-boot-testcontainers
 - testcontainers
 - junit-jupiter
 - testcontainers.postgresql
 - testcontainers.mongodb
 - testcontainers.kafka
 - testcontainers-redis

**Плагины**
 - build-helper-maven-plugin
 - maven-surefire-plugin
 - maven-failsafe-plugin

## Эндпоинты

Эндпоинты с примерами запросов описаны в swagger документации на gateway в разделе user-service:
http://localhost:8081/swagger-ui.html