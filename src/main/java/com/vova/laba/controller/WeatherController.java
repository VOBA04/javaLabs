package com.vova.laba.controller;

import com.vova.laba.aspect.annotation.RequestCounting;
import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.service.WeatherService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/weather")
@RequestCounting
@CrossOrigin
public class WeatherController {

  private WeatherService weatherService;

  public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<WeatherDisplayDto>> getAllWeather() {
    return ResponseEntity.of(weatherService.getAllWeather());
  }

  @GetMapping("/all/sorted")
  public ResponseEntity<List<WeatherDisplayDto>> getAllWeatherSorted() {
    return ResponseEntity.of(weatherService.getAllWeatherSorted());
  }

  @GetMapping("/{id}")
  public ResponseEntity<WeatherDisplayDto> getWeatherById(@PathVariable("id") Long id) {
    return ResponseEntity.of(weatherService.getWeatherById(id));
  }

  @PostMapping("/")
  public ResponseEntity<WeatherDisplayDto> createWeather(@RequestBody WeatherCreateDto weather) {
    return ResponseEntity.of(weatherService.saveWeather(weather));
  }

  @PostMapping("/forecasts")
  public ResponseEntity<List<WeatherDisplayDto>> createWeathers(
      @RequestBody List<WeatherCreateDto> forecasts) {
    return ResponseEntity.of(weatherService.saveWeathers(forecasts));
  }

  @PutMapping("/{id}")
  public ResponseEntity<WeatherDisplayDto> updateWeather(
      @PathVariable("id") Long id, @RequestBody WeatherCreateDto weather) {
    return ResponseEntity.of(weatherService.updateWeather(id, weather));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<WeatherDisplayDto> deleteWeather(@PathVariable("id") Long id) {
    return ResponseEntity.of(weatherService.deleteWeather(id));
  }
}
