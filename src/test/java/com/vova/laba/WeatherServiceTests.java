package com.vova.laba;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.serviceimpl.WeatherServiceImpl;
import com.vova.laba.utils.cache.GenericCache;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTests {

  @Mock private WeatherRepository weatherRepository;
  @Mock private CityRepository cityRepository;
  @Mock private GenericCache<Long, Weather> cache;

  @InjectMocks private WeatherServiceImpl weatherService;

  private Weather weather;
  private City city;

  @Spy private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() {
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
  }

  @Test
  void testGetWeatherById() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));

    Optional<WeatherDisplayDto> weatherOptional = weatherService.getWeatherById(1L);

    assertTrue(weatherOptional.isPresent());
    assertEquals(weather.getId(), weatherOptional.get().getId());
    verify(cache, times(1)).put(1L, weather);
  }

  @Test
  void testGetWeatherById_CacheHit() {
    when(cache.get(1L)).thenReturn(Optional.of(weather));

    Optional<WeatherDisplayDto> weatherOptional = weatherService.getWeatherById(1L);

    assertTrue(weatherOptional.isPresent());
    assertEquals(weather.getId(), weatherOptional.get().getId());
    verify(weatherRepository, never()).findById(anyLong());
    verify(cache, times(1)).put(1L, weather);
  }

  @Test
  void testGetWeatherById_CacheMiss() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));

    Optional<WeatherDisplayDto> weatherOptional = weatherService.getWeatherById(1L);

    assertTrue(weatherOptional.isPresent());
    assertEquals(weather.getId(), weatherOptional.get().getId());
    verify(cache).put(1L, weather);
  }

  @Test
  void testGetWeatherById_NotFound() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(weatherRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundExcepcion.class, () -> weatherService.getWeatherById(1L));
    verify(cache, never()).put(anyLong(), any(Weather.class));
  }

  @Test
  void testGetAllWeather() {
    when(weatherRepository.findAll()).thenReturn(Arrays.asList(weather, weather, weather));

    List<Weather> result =
        Arrays.asList(modelMapper.map(weatherService.getAllWeather(), Weather[].class));

    assertEquals(3, result.size());
    assertEquals(weather, result.get(1));
  }

  @Test
  void testSaveWeather() {
    when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    WeatherCreateDto weatherCreateDto = modelMapper.map(weather, WeatherCreateDto.class);
    weatherCreateDto.setCityId(city.getId());
    Weather result =
        modelMapper.map(weatherService.saveWeather(weatherCreateDto).get(), Weather.class);

    assertNotNull(result);
    assertEquals(weather, result);
  }

  @Test
  void testSaveWeatherForecasts() {
    when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    WeatherCreateDto weatherCreateDto = modelMapper.map(weather, WeatherCreateDto.class);
    weatherCreateDto.setCityId(city.getId());
    List<WeatherCreateDto> forecasts =
        Arrays.asList(weatherCreateDto, weatherCreateDto, weatherCreateDto);
    Optional<List<WeatherDisplayDto>> result = weatherService.saveWeathers(forecasts);

    assertTrue(result.isPresent());
    assertFalse(result.get().isEmpty());
    assertEquals(modelMapper.map(weather, WeatherDisplayDto.class), result.get().get(1));
    verify(weatherRepository, times(3)).save(any(Weather.class));
  }

  @Test
  void testUpdateWeather() {
    when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    WeatherCreateDto weatherCreateDto = modelMapper.map(weather, WeatherCreateDto.class);
    weatherCreateDto.setCityId(city.getId());
    Weather result =
        modelMapper.map(
            weatherService.updateWeather(weather.getId(), weatherCreateDto).get(), Weather.class);

    assertNotNull(result);
    assertEquals(weather, result);
    verify(cache, times(1)).remove(1L);
  }

  @Test
  void testDeleteWeather() {
    doNothing().when(weatherRepository).deleteById(1L);
    when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));

    Weather result = modelMapper.map(weatherService.deleteWeather(1L), Weather.class);

    assertEquals(weather, result);
    verify(weatherRepository, times(1)).deleteById(1L);
    verify(cache, times(1)).remove(1L);
  }
}
