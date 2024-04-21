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

import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.serviceimpl.CityServiceImpl;
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
class CityServiceTests {

  @Mock private CityRepository cityRepository;
  @Mock private WeatherRepository weatherRepository;
  @Mock private GenericCache<Long, City> cache;

  @InjectMocks private CityServiceImpl cityService;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  private City city;
  private Weather weather;

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
  void testGetCityById() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    Optional<CityDisplayDto> cityOptional = cityService.getCityById(1L);

    assertTrue(cityOptional.isPresent());
    assertEquals(city.getCityName(), cityOptional.get().getCityName());
    verify(cache, times(1)).put(1L, city);
  }

  @Test
  void testGetCityById_CacheHit() {
    when(cache.get(1L)).thenReturn(Optional.of(city));

    Optional<CityDisplayDto> cityOptional = cityService.getCityById(1L);

    assertTrue(cityOptional.isPresent());
    assertEquals(city.getCityName(), cityOptional.get().getCityName());
    verify(cityRepository, never()).findById(anyLong());
    verify(cache, times(1)).put(1L, city);
  }

  @Test
  void testGetCityById_CacheMiss() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    Optional<CityDisplayDto> cityOptional = cityService.getCityById(1L);

    assertTrue(cityOptional.isPresent());
    assertEquals(city.getCityName(), cityOptional.get().getCityName());
    verify(cache).put(1L, city);
  }

  @Test
  void testGetCityById_NotFound() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(cityRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundExcepcion.class, () -> cityService.getCityById(1L));
    verify(cache, never()).put(anyLong(), any(City.class));
  }

  @Test
  void testGetAllCities() {
    when(cityRepository.findAll()).thenReturn(Arrays.asList(city, city, city));

    List<City> result =
        Arrays.asList(modelMapper.map(cityService.getAllCities().get(), City[].class));

    assertEquals(3, result.size());
    assertEquals(city, result.get(1));
  }

  @Test
  void testSaveCity() {
    when(cityRepository.save(any(City.class))).thenReturn(city);

    City result =
        modelMapper.map(
            cityService.saveCity(modelMapper.map(city, CityInfoDto.class)).get(), City.class);

    assertNotNull(result);
    assertEquals(city, result);
  }

  @Test
  void testSaveCities() {
    when(cityRepository.save(any(City.class))).thenReturn(city);

    CityInfoDto cityInfo = modelMapper.map(city, CityInfoDto.class);
    List<CityInfoDto> cities = Arrays.asList(cityInfo, cityInfo, cityInfo);
    Optional<List<CityDisplayDto>> result = cityService.saveCities(cities);

    assertTrue(result.isPresent());
    assertFalse(result.get().isEmpty());
    assertEquals(modelMapper.map(city, CityDisplayDto.class), result.get().get(1));
    verify(cityRepository, times(3)).save(any(City.class));
  }

  @Test
  void testUpdateCity() {
    when(cityRepository.save(any(City.class))).thenReturn(city);

    City result =
        modelMapper.map(
            cityService.updateCity(city.getId(), modelMapper.map(city, CityInfoDto.class)).get(),
            City.class);

    assertNotNull(result);
    assertEquals(city, result);
    verify(cache, times(1)).remove(1L);
  }

  @Test
  void testDeleteCity() {
    doNothing().when(cityRepository).deleteById(1L);
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    City result = modelMapper.map(cityService.deleteCity(1L).get(), City.class);

    assertEquals(city, result);
    verify(cityRepository, times(1)).deleteById(1L);
    verify(cache, times(1)).remove(1L);
  }

  @Test
  void testGetAllCityWeather() {
    weather.setCity(new City());
    city.setWeather(Arrays.asList(weather));
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

    List<Weather> result =
        Arrays.asList(
            modelMapper.map(
                cityService.getAllCityWeather(city.getId()).get().getWeather(), Weather[].class));

    assertEquals(1, result.size());
    assertEquals(weather, result.get(0));

    city.setWeather(null);
    weather.setCity(city);
  }
}
