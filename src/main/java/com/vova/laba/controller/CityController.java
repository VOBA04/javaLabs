package com.vova.laba.controller;

import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.service.CityService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/city")
public class CityController {

  private final CityService cityService;

  @Autowired
  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<CityDisplayDto>> getAllCities() {
    return ResponseEntity.of(cityService.getAllCities());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CityDisplayDto> getCityById(@PathVariable("id") Long id) {
    return ResponseEntity.of(cityService.getCityById(id));
  }

  @PostMapping("/")
  public ResponseEntity<CityDisplayDto> createCity(@RequestBody CityInfoDto city) {
    return ResponseEntity.of(cityService.saveCity(city));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CityDisplayDto> updateCity(
      @PathVariable("id") Long id, @RequestBody CityInfoDto city) {
    return ResponseEntity.of(cityService.updateCity(id, city));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CityDispalyWithWeather> deleteCity(@PathVariable("id") Long id) {
    return ResponseEntity.of(cityService.deleteCity(id));
  }

  @GetMapping("/all_weather/{id}")
  public ResponseEntity<CityDispalyWithWeather> getAllCityWeather(@PathVariable("id") Long id) {
    return ResponseEntity.of(cityService.getAllCityWeather(id));
  }

  @GetMapping("/weather/{id}")
  public ResponseEntity<CityDispalyWithWeather> getCityWeatherByTemperature(
      @PathVariable("id") Long id,
      @RequestParam(name = "minTemp", defaultValue = "-273") Float minTemp,
      @RequestParam(name = "maxTemp", defaultValue = "100") Float maxTemp) {
    return ResponseEntity.of(cityService.getCityWeatherByTemperature(id, minTemp, maxTemp));
  }
}
