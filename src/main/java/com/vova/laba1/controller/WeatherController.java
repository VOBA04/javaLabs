package com.vova.laba1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vova.laba1.payload.WeatherResponse;
import com.vova.laba1.service.WeatherService;

@RestController
@RequestMapping("api/v1/weather")

public class WeatherController {

    WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam(value = "city") String city) {
        WeatherResponse weatherController = weatherService.getWeather(weatherService.getCoordinates(city));
        return new ResponseEntity<>(weatherController, HttpStatus.OK);
    }
}