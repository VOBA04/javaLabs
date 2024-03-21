package com.vova.laba.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vova.laba.payload.city.CityInfoDTO;
import com.vova.laba.payload.weather.WeatherInfoDTO;
import com.vova.laba.service.WeatherApiService;

@RestController
@RequestMapping("api/v1/weather")
public class WeatherApiController {

    WeatherApiService weatherApiService;

    public WeatherApiController(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    @GetMapping("/curr")
    public ResponseEntity<WeatherInfoDTO> getWeather(@RequestParam(value = "city") String city) {
        return ResponseEntity.of(weatherApiService.getWeather(weatherApiService.getCoordinates(city)));
    }

    @GetMapping("/days")
    public ResponseEntity<List<WeatherInfoDTO>> getFiveDaysWeather(@RequestParam(value = "city") String city) {
        return ResponseEntity.of(weatherApiService.getFiveDaysWeather(weatherApiService.getCoordinates(city)));
    }

    @GetMapping("/db/curr")
    public ResponseEntity<WeatherInfoDTO> getWeatherToDatabase(@RequestParam(value = "city") String city) {
        Optional<WeatherInfoDTO> weatherResponse = weatherApiService.getWeather(weatherApiService.getCoordinates(city));
        if (weatherResponse.isPresent()) {
            weatherApiService.addToDatabase(new CityInfoDTO(city), weatherResponse.get());
        }
        return ResponseEntity.of(weatherResponse);
    }

    @GetMapping("/db/days")
    public ResponseEntity<List<WeatherInfoDTO>> getFiveDaysWeatherToDatabase(
            @RequestParam(value = "city") String city) {
        Optional<List<WeatherInfoDTO>> weatherResponses = weatherApiService
                .getFiveDaysWeather(weatherApiService.getCoordinates(city));
        if (weatherResponses.isPresent()) {
            weatherApiService.addToDatabase(new CityInfoDTO(city), weatherResponses.get());
        }
        return ResponseEntity.of(weatherResponses);
    }
}