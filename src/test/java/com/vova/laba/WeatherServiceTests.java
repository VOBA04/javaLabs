package com.vova.laba;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.WeatherService;
import com.vova.laba.serviceimpl.WeatherServiceImpl;
import com.vova.laba.utils.cache.SimpleCache;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WeatherServiceTests {

  @MockBean private WeatherRepository weatherRepository;
  @MockBean private CityRepository cityRepository;

  private WeatherService weatherService;
  private Weather weather;
  private City city;
  private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() {
    weatherService =
        new WeatherServiceImpl(weatherRepository, modelMapper, new SimpleCache<>(), cityRepository);
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    city = new City();
    city.setId(1L);
    city.setCityName("Test City");

    weather = new Weather();
    weather.setId(1L);
    weather.setCity(city);
    weather.setTemp(20.0f);
    weather.setPressure(1000);
    weather.setHumidity(50);
    weather.setSpeed(5.0f);
    weather.setDeg(180);
    weather.setClouds(0);

    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
    when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));
  }

  @Test
  void testGetWeatherById() {
    Weather result = modelMapper.map(weatherService.getWeatherById(1L).get(), Weather.class);
    assertEquals(weather, result);
  }

  @Test
  void testGetAllWeather() {
    when(weatherRepository.findAll()).thenReturn(Arrays.asList(weather));
    List<Weather> result =
        Arrays.asList(modelMapper.map(weatherService.getAllWeather(), Weather[].class));
    assertEquals(1, result.size());
    assertEquals(weather, result.get(0));
  }

  @Test
  void testAddWeather() {
    when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
    WeatherCreateDto weatherCreateDto = modelMapper.map(weather, WeatherCreateDto.class);
    weatherCreateDto.setCityId(city.getId());
    Weather result =
        modelMapper.map(weatherService.saveWeather(weatherCreateDto).get(), Weather.class);
    assertEquals(weather, result);
  }

  @Test
  void testUpdateWeather() {
    when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
    WeatherCreateDto weatherCreateDto = modelMapper.map(weather, WeatherCreateDto.class);
    weatherCreateDto.setCityId(city.getId());
    Weather result =
        modelMapper.map(
            weatherService.updateWeather(weather.getId(), weatherCreateDto).get(), Weather.class);
    assertEquals(weather, result);
  }

  @Test
  void testDeleteWeather() {
    doNothing().when(weatherRepository).deleteById(1L);
    weatherService.deleteWeather(1L);
    verify(weatherRepository, times(1)).deleteById(1L);
  }
}
