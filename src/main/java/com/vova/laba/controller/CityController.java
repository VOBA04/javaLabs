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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDTO;
import com.vova.laba.dto.city.CityInfoDTO;
import com.vova.laba.service.CityService;

@RestController
@RequestMapping("/api/v2/city")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityDisplayDTO>> getAllCities() {
        return ResponseEntity.of(cityService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDisplayDTO> getCityById(@PathVariable("id") Long id) {
        return ResponseEntity.of(cityService.getCityById(id));
    }

    @PostMapping("/")
    public ResponseEntity<CityDisplayDTO> createCity(@RequestBody CityInfoDTO city) {
        return ResponseEntity.ok(cityService.saveCity(city));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDisplayDTO> updateCity(@PathVariable("id") Long id, @RequestBody CityInfoDTO city) {
        return ResponseEntity.ok(cityService.updateCity(id, city));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable("id") Long id) {
        if (cityService.deleteCity(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all_weather/{id}")
    public ResponseEntity<CityDispalyWithWeather> getAllCityWeather(@PathVariable("id") Long id) {
        return ResponseEntity.of(cityService.getAllCityWeather(id));
    }

    @GetMapping("/weather/{id}")
    public ResponseEntity<CityDispalyWithWeather> getCityWeatherByTemperature(@PathVariable("id") Long id,
            @RequestParam(name = "minTemp", defaultValue = "-273") Float minTemp,
            @RequestParam(name = "maxTemp", defaultValue = "100") Float maxTemp) {
        return ResponseEntity.of(cityService.getCityWeatherByTemperature(id, minTemp, maxTemp));
    }
}
