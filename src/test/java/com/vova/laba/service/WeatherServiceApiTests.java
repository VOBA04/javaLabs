package com.vova.laba.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.openweatherapi.CityCoordinatesResponse;
import com.vova.laba.dto.openweatherapi.CloudsData;
import com.vova.laba.dto.openweatherapi.OpenWeatherFiveDaysReport;
import com.vova.laba.dto.openweatherapi.OpenWeatherReport;
import com.vova.laba.dto.openweatherapi.TemperatureData;
import com.vova.laba.dto.openweatherapi.WeatherDate;
import com.vova.laba.dto.openweatherapi.WindData;
import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.dto.weather.WeatherInfoDto;
import com.vova.laba.exceptions.ApiException;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.serviceimpl.CityServiceImpl;
import com.vova.laba.serviceimpl.WeatherApiServiceImpl;
import com.vova.laba.serviceimpl.WeatherServiceImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("city", null);
    uriVariables.put("key", null);
    when(restTemplate.getForEntity(
            anyString(), eq(CityCoordinatesResponse[].class), eq(uriVariables)))
        .thenThrow(new BadRequestException(null));
    assertThrows(
        com.vova.laba.exceptions.BadRequestException.class,
        () -> weatherApiService.getCoordinates(null));
  }

  @Test
  void testGetCoordinates() {
    CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
    cityCoordinates.setLat(51.5073219f);
    cityCoordinates.setLon(-0.1276474f);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("city", "London");
    uriVariables.put("key", null);
    ResponseEntity<CityCoordinatesResponse[]> responseEntity =
        new ResponseEntity<CityCoordinatesResponse[]>(
            new CityCoordinatesResponse[] {cityCoordinates}, HttpStatus.OK);
    when(restTemplate.getForEntity(
            anyString(), eq(CityCoordinatesResponse[].class), eq(uriVariables)))
        .thenReturn(responseEntity);

    Optional<CityCoordinatesResponse> result = weatherApiService.getCoordinates("London");

    assertTrue(result.isPresent());
    assertEquals(cityCoordinates, result.get());
  }

  @Test
  void testGetWeather() {
    Optional<CityCoordinatesResponse> coords = Optional.empty();
    assertThrows(BadRequestException.class, () -> weatherApiService.getWeather(coords));

    CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
    cityCoordinates.setLat(51.5073219f);
    cityCoordinates.setLon(-0.1276474f);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", Float.toString(51.5073219f));
    uriVariables.put("lon", Float.toString(-0.1276474f));
    uriVariables.put("key", null);
    OpenWeatherReport openWeatherReport = new OpenWeatherReport();
    openWeatherReport.setClouds(new CloudsData(weather.getClouds()));
    openWeatherReport.setMain(
        new TemperatureData(weather.getTemp(), weather.getPressure(), weather.getHumidity()));
    openWeatherReport.setWind(new WindData(weather.getSpeed(), weather.getDeg()));
    openWeatherReport.setDt(0L);
    ResponseEntity<OpenWeatherReport> weatherReport =
        new ResponseEntity<OpenWeatherReport>(openWeatherReport, HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(OpenWeatherReport.class), eq(uriVariables)))
        .thenReturn(weatherReport);

    Optional<WeatherInfoDto> result = weatherApiService.getWeather(Optional.of(cityCoordinates));

    assertTrue(result.isPresent());
    result.get().setDate(new WeatherDate());
    assertEquals(modelMapper.map(weather, WeatherInfoDto.class), result.get());
  }

  @Test
  void testGetWeatherEmpty() {
    Optional<CityCoordinatesResponse> coords = Optional.empty();
    assertThrows(BadRequestException.class, () -> weatherApiService.getWeather(coords));

    CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
    cityCoordinates.setLat(51.5073219f);
    cityCoordinates.setLon(-0.1276474f);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", Float.toString(51.5073219f));
    uriVariables.put("lon", Float.toString(-0.1276474f));
    uriVariables.put("key", null);
    ResponseEntity<OpenWeatherReport> weatherReport =
        new ResponseEntity<OpenWeatherReport>(HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(OpenWeatherReport.class), eq(uriVariables)))
        .thenReturn(weatherReport);

    Optional<CityCoordinatesResponse> cityCoordsOptional = Optional.of(cityCoordinates);
    assertThrows(ApiException.class, () -> weatherApiService.getWeather(cityCoordsOptional));
  }

  @Test
  void testGetFiveDaysWeather() {
    Optional<CityCoordinatesResponse> coords = Optional.empty();
    assertThrows(BadRequestException.class, () -> weatherApiService.getFiveDaysWeather(coords));

    CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
    cityCoordinates.setLat(51.5073219f);
    cityCoordinates.setLon(-0.1276474f);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", Float.toString(51.5073219f));
    uriVariables.put("lon", Float.toString(-0.1276474f));
    uriVariables.put("key", null);
    OpenWeatherReport openWeatherReport = new OpenWeatherReport();
    openWeatherReport.setClouds(new CloudsData(weather.getClouds()));
    openWeatherReport.setMain(
        new TemperatureData(weather.getTemp(), weather.getPressure(), weather.getHumidity()));
    openWeatherReport.setWind(new WindData(weather.getSpeed(), weather.getDeg()));
    openWeatherReport.setDt(0L);
    OpenWeatherFiveDaysReport fiveDaysReport = new OpenWeatherFiveDaysReport();
    OpenWeatherReport[] openWeatherReports = new OpenWeatherReport[40];
    Arrays.fill(openWeatherReports, openWeatherReport);
    fiveDaysReport.setList(openWeatherReports);
    ResponseEntity<OpenWeatherFiveDaysReport> weatherReport =
        new ResponseEntity<OpenWeatherFiveDaysReport>(fiveDaysReport, HttpStatus.OK);
    when(restTemplate.getForEntity(
            anyString(), eq(OpenWeatherFiveDaysReport.class), eq(uriVariables)))
        .thenReturn(weatherReport);

    Optional<List<WeatherInfoDto>> result =
        weatherApiService.getFiveDaysWeather(Optional.of(cityCoordinates));

    assertTrue(result.isPresent());
    assertEquals(5, result.get().size());
    result.get().get(1).setDate(new WeatherDate());
    assertEquals(modelMapper.map(weather, WeatherInfoDto.class), result.get().get(1));
  }

  @Test
  void testGetFiveDaysWeatherEmptyBody() {
    Optional<CityCoordinatesResponse> coords = Optional.empty();
    assertThrows(BadRequestException.class, () -> weatherApiService.getFiveDaysWeather(coords));

    CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
    cityCoordinates.setLat(51.5073219f);
    cityCoordinates.setLon(-0.1276474f);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", Float.toString(51.5073219f));
    uriVariables.put("lon", Float.toString(-0.1276474f));
    uriVariables.put("key", null);
    ResponseEntity<OpenWeatherFiveDaysReport> weatherReport =
        new ResponseEntity<OpenWeatherFiveDaysReport>(HttpStatus.OK);
    when(restTemplate.getForEntity(
            anyString(), eq(OpenWeatherFiveDaysReport.class), eq(uriVariables)))
        .thenReturn(weatherReport);

    Optional<CityCoordinatesResponse> cityCoordsOptional = Optional.of(cityCoordinates);
    assertThrows(
        ApiException.class, () -> weatherApiService.getFiveDaysWeather(cityCoordsOptional));
  }

  @Test
  void testGetFiveDaysWeatherEmptyList() {
    Optional<CityCoordinatesResponse> coords = Optional.empty();
    assertThrows(BadRequestException.class, () -> weatherApiService.getFiveDaysWeather(coords));

    CityCoordinatesResponse cityCoordinates = new CityCoordinatesResponse();
    cityCoordinates.setLat(51.5073219f);
    cityCoordinates.setLon(-0.1276474f);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", Float.toString(51.5073219f));
    uriVariables.put("lon", Float.toString(-0.1276474f));
    uriVariables.put("key", null);
    OpenWeatherFiveDaysReport fiveDaysReport = new OpenWeatherFiveDaysReport();
    fiveDaysReport.setList(new OpenWeatherReport[0]);
    ResponseEntity<OpenWeatherFiveDaysReport> weatherReport =
        new ResponseEntity<OpenWeatherFiveDaysReport>(fiveDaysReport, HttpStatus.OK);
    when(restTemplate.getForEntity(
            anyString(), eq(OpenWeatherFiveDaysReport.class), eq(uriVariables)))
        .thenReturn(weatherReport);

    Optional<CityCoordinatesResponse> cityCoordsOptional = Optional.of(cityCoordinates);
    assertThrows(
        ApiException.class, () -> weatherApiService.getFiveDaysWeather(cityCoordsOptional));
  }

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
