package com.vova.laba.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.weather.WeatherInfoDto;
import com.vova.laba.serviceimpl.WeatherApiServiceImpl;
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
class WeatherApiControllerTests {

  @Mock private WeatherApiServiceImpl weatherApiService;

  @InjectMocks private WeatherApiController weatherApiController;

  private WeatherInfoDto weather = new WeatherInfoDto();

  @BeforeEach
  public void setUp() {
    weather.setTemp(20.0f);
    weather.setPressure(1000);
    weather.setHumidity(50);
    weather.setSpeed(5.0f);
    weather.setDeg(180);
    weather.setClouds(0);
  }

  @Test
  void testGetWeather() {
    when(weatherApiService.getWeather(weatherApiService.getCoordinates("Test City")))
        .thenReturn(Optional.of(weather));

    ResponseEntity<WeatherInfoDto> result = weatherApiController.getWeather("Test City");

    assertEquals(weather, result.getBody());
  }

  @Test
  void testGetFiveDaysWeather() {
    when(weatherApiService.getFiveDaysWeather(weatherApiService.getCoordinates("Test City")))
        .thenReturn(Optional.of(Arrays.asList(weather, weather, weather, weather, weather)));

    ResponseEntity<List<WeatherInfoDto>> result =
        weatherApiController.getFiveDaysWeather("Test City");

    assertEquals(5, result.getBody().size());
    assertEquals(weather, result.getBody().get(1));
  }

  @Test
  void testGetWeatherToDatabase() {
    when(weatherApiService.getWeather(weatherApiService.getCoordinates("Test City")))
        .thenReturn(Optional.of(weather));

    ResponseEntity<WeatherInfoDto> result = weatherApiController.getWeatherToDatabase("Test City");

    verify(weatherApiService, times(1)).addToDatabase(new CityInfoDto("Test City"), weather);
    assertEquals(weather, result.getBody());
  }

  @Test
  void testGetWeatherToDatabaseEmpty() {
    when(weatherApiService.getWeather(weatherApiService.getCoordinates("Test City")))
        .thenReturn(Optional.empty());

    ResponseEntity<WeatherInfoDto> result = weatherApiController.getWeatherToDatabase("Test City");

    verify(weatherApiService, never()).addToDatabase(new CityInfoDto("Test City"), weather);
    ResponseEntity<WeatherInfoDto> expected = ResponseEntity.of(Optional.empty());
    assertEquals(expected, result);
  }

  @Test
  void testGetFiveDaysWeatherToDatabase() {
    when(weatherApiService.getFiveDaysWeather(weatherApiService.getCoordinates("Test City")))
        .thenReturn(Optional.of(Arrays.asList(weather, weather, weather, weather, weather)));

    ResponseEntity<List<WeatherInfoDto>> result =
        weatherApiController.getFiveDaysWeatherToDatabase("Test City");

    verify(weatherApiService, times(1))
        .addToDatabase(
            new CityInfoDto("Test City"),
            Arrays.asList(weather, weather, weather, weather, weather));
    assertEquals(5, result.getBody().size());
    assertEquals(weather, result.getBody().get(1));
  }

  @Test
  void testGetFiveDaysWeatherToDatabaseEmpty() {
    when(weatherApiService.getFiveDaysWeather(weatherApiService.getCoordinates("Test City")))
        .thenReturn(Optional.empty());

    ResponseEntity<List<WeatherInfoDto>> result =
        weatherApiController.getFiveDaysWeatherToDatabase("Test City");

    verify(weatherApiService, never())
        .addToDatabase(
            new CityInfoDto("Test City"),
            Arrays.asList(weather, weather, weather, weather, weather));
    ResponseEntity<List<WeatherInfoDto>> expected = ResponseEntity.of(Optional.empty());
    assertEquals(expected, result);
  }
}
