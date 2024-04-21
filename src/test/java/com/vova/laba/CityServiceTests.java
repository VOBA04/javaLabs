package com.vova.laba;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.CityService;
import com.vova.laba.serviceimpl.CityServiceImpl;
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
class CityServiceTests {

  @MockBean private CityRepository cityRepository;
  @MockBean private WeatherRepository weatherRepository;

  private CityService cityService;
  private City city;
  private Weather weather;
  private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() {
    cityService =
        new CityServiceImpl(cityRepository, weatherRepository, modelMapper, new SimpleCache<>());
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
  void testGetCityById() {
    City result = modelMapper.map(cityService.getCityById(1L).get(), City.class);
    assertEquals(city, result);
  }

  @Test
  void testGetAllCities() {
    when(cityRepository.findAll()).thenReturn(Arrays.asList(city));
    List<City> result =
        Arrays.asList(modelMapper.map(cityService.getAllCities().get(), City[].class));
    assertEquals(1, result.size());
    assertEquals(city, result.get(0));
  }

  @Test
  void testAddCity() {
    when(cityRepository.save(any(City.class))).thenReturn(city);
    City result =
        modelMapper.map(
            cityService.saveCity(modelMapper.map(city, CityInfoDto.class)).get(), City.class);
    assertEquals(city, result);
  }

  @Test
  void testUpdateCity() {
    when(cityRepository.save(any(City.class))).thenReturn(city);
    City result =
        modelMapper.map(
            cityService.updateCity(city.getId(), modelMapper.map(city, CityInfoDto.class)).get(),
            City.class);
    assertEquals(city, result);
  }

  @Test
  void testDeleteCity() {
    doNothing().when(cityRepository).deleteById(1L);
    cityService.deleteCity(1L);
    verify(cityRepository, times(1)).deleteById(1L);
  }

  @Test
  void testGetAllCityWeather() {
    weather.setCity(new City());
    city.setWeather(Arrays.asList(weather));
    List<City> cities = Arrays.asList(city);
    when(cityRepository.findAll()).thenReturn(cities);
    when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));
    List<Weather> result =
        Arrays.asList(
            modelMapper.map(
                cityService.getAllCityWeather(city.getId()).get().getWeather(), Weather[].class));
    assertEquals(1, result.size());
    assertEquals(weather, result.get(0));
  }
}
