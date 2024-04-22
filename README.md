# Информация о лабораторной работе

**Тема:** Погода

**Реализация:** Реализованы эндпоинты для создания и управления пользователями, городами и погодой. Так же предусмотрен парсинг API текущей погоды и погоды на пять дней вперед с сервиса https://openweathermap.org/ и сохранение полученных данных в базу данных. Присутсвует логирование всех запросов пользователя и ошибок. Запросы можно производить чеерез Swagger. Реализованы bulk post запросы.

**Использованные технологии:** OpenJDK 21, Spring Boot 3.2.3, Maven, PostgreSQL 14, Swagger

**Переменные среды:** Перед запуском проекта нужно поместить Ваш API token в переменные среды, чтобы сервис мог выполнять Get-запросы на OpenWeatherMap.

```bash
export KEY=Key
```

где Key - ваш токен.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VOBA04_javaLabs&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=VOBA04_javaLabs)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=VOBA04_javaLabs&metric=coverage)](https://sonarcloud.io/summary/new_code?id=VOBA04_javaLabs)
