package com.vova.laba.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
        WeatherInfoDTO weatherResponse = weatherApiService.getWeather(weatherApiService.getCoordinates(city));
        return new ResponseEntity<>(weatherResponse, HttpStatus.OK);
    }

    @GetMapping("/days")
    public ResponseEntity<List<WeatherInfoDTO>> getFiveDaysWeather(@RequestParam(value = "city") String city) {
        List<WeatherInfoDTO> weatherResponses = weatherApiService
                .getFiveDaysWeather(weatherApiService.getCoordinates(city));
        return new ResponseEntity<>(weatherResponses, HttpStatus.OK);
    }

    @GetMapping("/db/curr")
    public ResponseEntity<WeatherInfoDTO> getWeatherToDatabase(@RequestParam(value = "city") String city) {
        WeatherInfoDTO weatherResponse = weatherApiService.getWeather(weatherApiService.getCoordinates(city));
        weatherApiService.addToDatabase(new CityInfoDTO(city), weatherResponse);
        return new ResponseEntity<>(weatherResponse, HttpStatus.OK);
    }

    @GetMapping("/db/days")
    public ResponseEntity<List<WeatherInfoDTO>> getFiveDaysWeatherToDatabase(
            @RequestParam(value = "city") String city) {
        List<WeatherInfoDTO> weatherResponses = weatherApiService
                .getFiveDaysWeather(weatherApiService.getCoordinates(city));
        weatherApiService.addToDatabase(new CityInfoDTO(city), weatherResponses);
        return new ResponseEntity<>(weatherResponses, HttpStatus.OK);
    }
}