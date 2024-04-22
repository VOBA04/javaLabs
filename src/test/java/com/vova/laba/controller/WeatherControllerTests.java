package com.vova.laba.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.weather.WeatherCityInfoDto;
import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.serviceimpl.WeatherServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTests {

  @Mock private WeatherServiceImpl weatherService;

  @InjectMocks private WeatherController weatherController;

  private WeatherDisplayDto weather = new WeatherDisplayDto();
  private WeatherCreateDto weatherCreate = new WeatherCreateDto();

  @BeforeEach
  public void setUp() {
    weather.setId(1L);
    weather.setCity(new WeatherCityInfoDto());
    weather.setTemp(20.0f);
    weather.setPressure(1000);
    weather.setHumidity(50);
    weather.setSpeed(5.0f);
    weather.setDeg(180);
    weather.setClouds(0);

    weatherCreate.setCityId(1L);
    weatherCreate.setTemp(20.0f);
    weatherCreate.setPressure(1000);
    weatherCreate.setHumidity(50);
    weatherCreate.setSpeed(5.0f);
    weatherCreate.setDeg(180);
    weatherCreate.setClouds(0);
  }

  @Test
  void testGetAllWeathers() {
    when(weatherService.getAllWeather())
        .thenReturn(Optional.of(Arrays.asList(weather, weather, weather)));

    ResponseEntity<List<WeatherDisplayDto>> result = weatherController.getAllWeather();

    assertEquals(3, result.getBody().size());
    assertEquals(weather, result.getBody().get(1));
  }

  @Test
  void testGetWeatherById() {
    when(weatherService.getWeatherById(1L)).thenReturn(Optional.of(weather));

    ResponseEntity<WeatherDisplayDto> result = weatherController.getWeatherById(1L);

    assertEquals(weather, result.getBody());
  }

  @Test
  void testCreateWeather() {
    when(weatherService.saveWeather(weatherCreate)).thenReturn(Optional.of(weather));

    ResponseEntity<WeatherDisplayDto> result = weatherController.createWeather(weatherCreate);

    assertEquals(weather, result.getBody());
  }

  @Test
  void testCreateWeathers() {
    when(weatherService.saveWeathers(Arrays.asList(weatherCreate, weatherCreate, weatherCreate)))
        .thenReturn(Optional.of(Arrays.asList(weather, weather, weather)));

    ResponseEntity<List<WeatherDisplayDto>> result =
        weatherController.createWeathers(
            Arrays.asList(weatherCreate, weatherCreate, weatherCreate));

    assertEquals(3, result.getBody().size());
    assertEquals(weather, result.getBody().get(1));
  }

  @Test
  void testUpdateWeather() {
    when(weatherService.updateWeather(1L, weatherCreate)).thenReturn(Optional.of(weather));

    ResponseEntity<WeatherDisplayDto> result = weatherController.updateWeather(1L, weatherCreate);

    assertEquals(weather, result.getBody());
  }

  @Test
  void testDeleteWeather() {
    when(weatherService.deleteWeather(1L)).thenReturn(Optional.of(weather));

    ResponseEntity<WeatherDisplayDto> result = weatherController.deleteWeather(1L);

    assertEquals(weather, result.getBody());
  }
}
