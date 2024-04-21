package com.vova.laba.serviceimpl;

import com.vova.laba.aspect.Logging;
import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.openweatherapi.CityCoordinatesResponse;
import com.vova.laba.dto.openweatherapi.OpenWeatherFiveDaysReport;
import com.vova.laba.dto.openweatherapi.OpenWeatherReport;
import com.vova.laba.dto.openweatherapi.WeatherDate;
import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherInfoDto;
import com.vova.laba.exceptions.ApiException;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.service.CityService;
import com.vova.laba.service.WeatherApiService;
import com.vova.laba.service.WeatherService;
import jakarta.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiServiceImpl implements WeatherApiService {
  @Value("${weatherservice.key}")
  private String key;

  private final CityService cityService;
  private final WeatherService weatherService;
  private final ModelMapper modelMapper;

  private String cityName = "Wrong city name";

  @Autowired
  public WeatherApiServiceImpl(
      CityService cityService, WeatherService weatherService, ModelMapper modelMapper) {
    this.cityService = cityService;
    this.weatherService = weatherService;
    this.modelMapper = modelMapper;
  }

  @Logging
  @Override
  public Optional<CityCoordinatesResponse> getCoordinates(String city) throws BadRequestException {
    String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={key}";
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("city", city);
    uriVariables.put("key", key);
    ResponseEntity<CityCoordinatesResponse[]> responseEntity;
    try {
      responseEntity =
          restTemplate.getForEntity(apiUrl, CityCoordinatesResponse[].class, uriVariables);
    } catch (RestClientException e) {
      throw new BadRequestException(cityName);
    }
    CityCoordinatesResponse[] responseBody = responseEntity.getBody();
    if (responseBody != null && responseBody.length > 0) {
      return Optional.of(responseBody[0]);
    } else {
      return Optional.empty();
    }
  }

  @Logging
  @Override
  public Optional<WeatherInfoDto> getWeather(Optional<CityCoordinatesResponse> coordOptional)
      throws BadRequestException, ApiException {
    if (!coordOptional.isPresent()) {
      throw new BadRequestException(cityName);
    }
    CityCoordinatesResponse coord = coordOptional.get();
    String apiUrl =
        "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}&units=metric";
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", coord.getLat().toString());
    uriVariables.put("lon", coord.getLon().toString());
    uriVariables.put("key", key);
    ResponseEntity<OpenWeatherReport> responseEntity =
        restTemplate.getForEntity(apiUrl, OpenWeatherReport.class, uriVariables);
    OpenWeatherReport responseBody = responseEntity.getBody();
    if (responseBody != null) {
      WeatherInfoDto weatherResponse = new WeatherInfoDto();
      weatherResponse.setTemp(responseBody.getMain().getTemp());
      weatherResponse.setHumidity(responseBody.getMain().getHumidity());
      weatherResponse.setPressure(responseBody.getMain().getPressure());
      weatherResponse.setSpeed(responseBody.getWind().getSpeed());
      weatherResponse.setDeg(responseBody.getWind().getDeg());
      weatherResponse.setClouds(responseBody.getClouds().getAll());
      String date =
          new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(responseBody.getDt() * 1000));
      weatherResponse.setDate(WeatherDate.stringToDate(date));
      return Optional.of(weatherResponse);
    }
    throw new ApiException("Error receiving data from OpenWeatherAPI");
  }

  @Logging
  @Override
  public Optional<List<WeatherInfoDto>> getFiveDaysWeather(
      Optional<CityCoordinatesResponse> coordOptional) throws BadRequestException, ApiException {
    if (!coordOptional.isPresent()) {
      throw new BadRequestException(cityName);
    }
    CityCoordinatesResponse coord = coordOptional.get();
    String apiUrl =
        "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={key}&units=metric";
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("lat", coord.getLat().toString());
    uriVariables.put("lon", coord.getLon().toString());
    uriVariables.put("key", key);
    ResponseEntity<OpenWeatherFiveDaysReport> responseEntity =
        restTemplate.getForEntity(apiUrl, OpenWeatherFiveDaysReport.class, uriVariables);
    OpenWeatherFiveDaysReport responseBody = responseEntity.getBody();
    if (responseBody != null) {
      OpenWeatherReport[] responseList = responseBody.getList();
      if (responseList.length > 0) {
        List<WeatherInfoDto> weatherResponses = new ArrayList<>();
        for (int i = 0; i < 40; i += 8) {
          WeatherInfoDto weatherResponse = new WeatherInfoDto();
          weatherResponse.setTemp(responseList[i].getMain().getTemp());
          weatherResponse.setHumidity(responseList[i].getMain().getHumidity());
          weatherResponse.setPressure(responseList[i].getMain().getPressure());
          weatherResponse.setSpeed(responseList[i].getWind().getSpeed());
          weatherResponse.setDeg(responseList[i].getWind().getDeg());
          weatherResponse.setClouds(responseList[i].getClouds().getAll());
          String date =
              new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                  .format(new Date(responseList[i].getDt() * 1000));
          weatherResponse.setDate(WeatherDate.stringToDate(date));
          weatherResponses.add(weatherResponse);
        }
        return Optional.of(weatherResponses);
      }
    }
    throw new ApiException("Error receiving data from OpenWeatherAPI");
  }

  @Logging
  @Override
  @Transactional
  public void addToDatabase(CityInfoDto city, WeatherInfoDto weather)
      throws BadRequestException, ApiException {
    Optional<CityDisplayDto> cityOptional = cityService.saveCity(city);
    if (cityOptional.isPresent()) {
      CityDisplayDto cityModel = cityOptional.get();
      WeatherCreateDto weatherDto = modelMapper.map(weather, WeatherCreateDto.class);
      weatherDto.setCityId(cityModel.getId());
      weatherService.saveWeather(weatherDto);
    }
  }

  @Logging
  @Override
  @Transactional
  public void addToDatabase(CityInfoDto city, List<WeatherInfoDto> weathers)
      throws BadRequestException, ApiException {
    Optional<CityDisplayDto> cityOptional = cityService.saveCity(city);
    if (cityOptional.isPresent()) {
      CityDisplayDto cityModel = cityOptional.get();
      for (int i = 0; i < weathers.size(); i++) {
        WeatherCreateDto weatherDto;
        weatherDto = modelMapper.map(weathers.get(i), WeatherCreateDto.class);
        weatherDto.setCityId(cityModel.getId());
        weatherService.saveWeather(weatherDto);
      }
    }
  }
}
