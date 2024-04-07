package com.vova.laba.controller;

import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.weather.WeatherInfoDto;
import com.vova.laba.service.WeatherApiService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/weather")
public class WeatherApiController {

  WeatherApiService weatherApiService;

  @Autowired
  public WeatherApiController(WeatherApiService weatherApiService) {
    this.weatherApiService = weatherApiService;
  }

  @GetMapping("/curr")
  public ResponseEntity<WeatherInfoDto> getWeather(@RequestParam(value = "city") String city) {
    return ResponseEntity.of(weatherApiService.getWeather(weatherApiService.getCoordinates(city)));
  }

  @GetMapping("/days")
  public ResponseEntity<List<WeatherInfoDto>> getFiveDaysWeather(
      @RequestParam(value = "city") String city) {
    return ResponseEntity.of(
        weatherApiService.getFiveDaysWeather(weatherApiService.getCoordinates(city)));
  }

  @GetMapping("/db/curr")
  public ResponseEntity<WeatherInfoDto> getWeatherToDatabase(
      @RequestParam(value = "city") String city) {

    Optional<WeatherInfoDto> weatherResponse =
        weatherApiService.getWeather(weatherApiService.getCoordinates(city));
    if (weatherResponse.isPresent()) {
      weatherApiService.addToDatabase(new CityInfoDto(city), weatherResponse.get());
    }
    return ResponseEntity.of(weatherResponse);
  }

  @GetMapping("/db/days")
  public ResponseEntity<List<WeatherInfoDto>> getFiveDaysWeatherToDatabase(
      @RequestParam(value = "city") String city) {
    Optional<List<WeatherInfoDto>> weatherResponses =
        weatherApiService.getFiveDaysWeather(weatherApiService.getCoordinates(city));
    if (weatherResponses.isPresent()) {
      weatherApiService.addToDatabase(new CityInfoDto(city), weatherResponses.get());
    }
    return ResponseEntity.of(weatherResponses);
  }
}