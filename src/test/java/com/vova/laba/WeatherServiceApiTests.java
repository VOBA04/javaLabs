package com.vova.laba;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.dto.weather.WeatherInfoDto;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.serviceimpl.CityServiceImpl;
import com.vova.laba.serviceimpl.WeatherApiServiceImpl;
import com.vova.laba.serviceimpl.WeatherServiceImpl;
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
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class WeatherServiceApiTests {

  @Mock private CityServiceImpl cityService;
  @Mock private WeatherServiceImpl weatherService;
  @Mock private RestTemplate restTemplate;

  @InjectMocks WeatherApiServiceImpl weatherApiService;

  @Spy ModelMapper modelMapper = new ModelMapper();

  Weather weather;
  City city;

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
  void testGetCoordinatesThrowing() {
    assertThrows(
        com.vova.laba.exceptions.BadRequestException.class,
        () -> weatherApiService.getCoordinates(null));
  }

  // @Test
  // void testGetCoordinates() {
  //   CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
  //   cityCoordinates.setLat(51.5073219f);
  //   cityCoordinates.setLon(-0.1276474f);
  //   Map<String, String> uriVariables = new HashMap<>();
  //   uriVariables.put("city", "London");
  //   uriVariables.put("key", "2df91c780bf53b83da4437002a1caff1");
  //   ResponseEntity<CityCoordinatesResponse[]> responseEntity =
  //       new ResponseEntity<CityCoordinatesResponse[]>(
  //           new CityCoordinatesResponse[] {cityCoordinates}, HttpStatus.OK);
  //   String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={key}";
  //   when(restTemplate.getForEntity(apiUrl, CityCoordinatesResponse[].class, uriVariables))
  //       .thenReturn(responseEntity);

  //   Optional<CityCoordinatesResponse> result = weatherApiService.getCoordinates("London");

  //   assertTrue(result.isPresent());
  //   assertEquals(cityCoordinates, result.get());
  // }

  @Test
  void testAddToDatabaseWeather() {
    CityDisplayDto cityDisplayDto = modelMapper.map(city, CityDisplayDto.class);
    when(cityService.saveCity(any(CityInfoDto.class))).thenReturn(Optional.of(cityDisplayDto));
    weather.setCity(city);
    WeatherDisplayDto weatherDisplayDto = modelMapper.map(weather, WeatherDisplayDto.class);
    when(weatherService.saveWeather(any(WeatherCreateDto.class)))
        .thenReturn(Optional.of(weatherDisplayDto));
    weather.setCity(null);

    weatherApiService.addToDatabase(
        modelMapper.map(city, CityInfoDto.class), modelMapper.map(weather, WeatherInfoDto.class));

    verify(cityService, times(1)).saveCity(any(CityInfoDto.class));
    verify(weatherService, times(1)).saveWeather(any(WeatherCreateDto.class));
  }

  @Test
  void testAddToDatabaseWeathers() {
    CityDisplayDto cityDisplayDto = modelMapper.map(city, CityDisplayDto.class);
    when(cityService.saveCity(any(CityInfoDto.class))).thenReturn(Optional.of(cityDisplayDto));
    weather.setCity(city);
    WeatherDisplayDto weatherDisplayDto = modelMapper.map(weather, WeatherDisplayDto.class);
    when(weatherService.saveWeather(any(WeatherCreateDto.class)))
        .thenReturn(Optional.of(weatherDisplayDto));
    weather.setCity(null);

    WeatherInfoDto weatherInfoDto = modelMapper.map(weather, WeatherInfoDto.class);
    List<WeatherInfoDto> weathers = Arrays.asList(weatherInfoDto, weatherInfoDto, weatherInfoDto);
    weatherApiService.addToDatabase(modelMapper.map(city, CityInfoDto.class), weathers);

    verify(cityService, times(1)).saveCity(any(CityInfoDto.class));
    verify(weatherService, times(3)).saveWeather(any(WeatherCreateDto.class));
  }
}
