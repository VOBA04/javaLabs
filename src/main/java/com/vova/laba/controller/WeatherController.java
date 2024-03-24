package com.vova.laba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vova.laba.dto.weather.WeatherCreateDTO;
import com.vova.laba.dto.weather.WeatherDisplayDTO;
import com.vova.laba.dto.weather.WeatherInfoDTO;
import com.vova.laba.service.WeatherService;

@RestController
@RequestMapping("/api/v2/weather")
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WeatherDisplayDTO>> getAllWeather() {
        return ResponseEntity.of(weatherService.getAllWeather());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherDisplayDTO> getWeatherById(@PathVariable("id") Long id) {
        return ResponseEntity.of(weatherService.getWeatherById(id));
    }

    @PostMapping("/")
    public ResponseEntity<WeatherInfoDTO> createWeather(@RequestBody WeatherCreateDTO weather) {
        return ResponseEntity.ok(weatherService.saveWeather(weather));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherInfoDTO> updateWeather(@PathVariable("id") Long id,
            @RequestBody WeatherCreateDTO weather) {
        return ResponseEntity.ok(weatherService.updateWeather(id, weather));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeather(@PathVariable("id") Long id) {
        if (weatherService.deleteWeather(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
