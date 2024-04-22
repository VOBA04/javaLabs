package com.vova.laba.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityWeatherInfoDto;
import com.vova.laba.serviceimpl.CityServiceImpl;
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
class CityControllerTests {

  @Mock private CityServiceImpl cityService;

  @InjectMocks private CityController cityController;

  private CityDisplayDto city = new CityDisplayDto();
  private CityWeatherInfoDto weather = new CityWeatherInfoDto();

  @BeforeEach
  public void setUp() {
    city.setId(1L);
    city.setCityName("Test City");

    weather.setId(1L);
    weather.setTemp(20.0f);
    weather.setPressure(1000);
    weather.setHumidity(50);
    weather.setSpeed(5.0f);
    weather.setDeg(180);
    weather.setClouds(0);
  }

  @Test
  void testGetAllCities() {
    when(cityService.getAllCities()).thenReturn(Optional.of(Arrays.asList(city, city, city)));

    ResponseEntity<List<CityDisplayDto>> result = cityController.getAllCities();

    assertEquals(3, result.getBody().size());
    assertEquals(city, result.getBody().get(1));
  }

  @Test
  void testGetCityById() {
    when(cityService.getCityById(1L)).thenReturn(Optional.of(city));

    ResponseEntity<CityDisplayDto> result = cityController.getCityById(1L);

    assertEquals(city, result.getBody());
  }

  @Test
  void testCreateCity() {
    when(cityService.saveCity(city)).thenReturn(Optional.of(city));

    ResponseEntity<CityDisplayDto> result = cityController.createCity(city);

    assertEquals(city, result.getBody());
  }

  @Test
  void testCreateCities() {
    when(cityService.saveCities(Arrays.asList(city, city, city)))
        .thenReturn(Optional.of(Arrays.asList(city, city, city)));

    ResponseEntity<List<CityDisplayDto>> result =
        cityController.createCities(Arrays.asList(city, city, city));

    assertEquals(3, result.getBody().size());
    assertEquals(city, result.getBody().get(1));
  }

  @Test
  void testUpdateCity() {
    when(cityService.updateCity(1L, city)).thenReturn(Optional.of(city));

    ResponseEntity<CityDisplayDto> result = cityController.updateCity(1L, city);

    assertEquals(city, result.getBody());
  }

  @Test
  void testDeleteCity() {
    CityDispalyWithWeather cityDispalyWithWeather = new CityDispalyWithWeather();
    cityDispalyWithWeather.setCityName(city.getCityName());
    cityDispalyWithWeather.setId(city.getId());
    cityDispalyWithWeather.setWeather(Arrays.asList(weather, weather, weather));
    when(cityService.deleteCity(1L)).thenReturn(Optional.of(cityDispalyWithWeather));

    ResponseEntity<CityDispalyWithWeather> result = cityController.deleteCity(1L);

    assertEquals(cityDispalyWithWeather, result.getBody());
  }

  @Test
  void testGetAllCityWeather() {
    CityDispalyWithWeather cityDispalyWithWeather = new CityDispalyWithWeather();
    cityDispalyWithWeather.setCityName(city.getCityName());
    cityDispalyWithWeather.setId(city.getId());
    cityDispalyWithWeather.setWeather(Arrays.asList(weather, weather, weather));
    when(cityService.getAllCityWeather(1L)).thenReturn(Optional.of(cityDispalyWithWeather));

    ResponseEntity<CityDispalyWithWeather> result = cityController.getAllCityWeather(1L);

    assertEquals(cityDispalyWithWeather, result.getBody());
  }

  @Test
  void testGetCityWeatherByTemperature() {
    CityDispalyWithWeather cityDispalyWithWeather = new CityDispalyWithWeather();
    cityDispalyWithWeather.setCityName(city.getCityName());
    cityDispalyWithWeather.setId(city.getId());
    cityDispalyWithWeather.setWeather(Arrays.asList(weather, weather, weather));
    when(cityService.getCityWeatherByTemperature(1L, -255f, 100f))
        .thenReturn(Optional.of(cityDispalyWithWeather));

    ResponseEntity<CityDispalyWithWeather> result =
        cityController.getCityWeatherByTemperature(1L, -255f, 100f);

    assertEquals(cityDispalyWithWeather, result.getBody());
  }
}
