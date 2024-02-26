# Информация о лабораторной работе

**Тема:** Погода

**Реализация:** Отправление get-запроса на сервис https://openweathermap.org/ и получения из него данных по API о текущей температуре, давлению, влажности, скорости ветра и его направлению, а также об облачности. В запросе указывается город, погоду которого хотим узнать. 

**Использованные технологии:** OpenJDK 21, Spring Boot 3.2.3, Maven

**Файл настроек:** В файл *application.yml* следует написать Ваш API token от OpenWeatherMap в виде:
```yml
weatherservice:
  key: Key
```
где Key - ваш токен.  

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VOBA04_javaLabs&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=VOBA04_javaLabs)