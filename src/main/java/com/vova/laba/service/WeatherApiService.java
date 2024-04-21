package com.vova.laba.service;

import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.openweatherapi.CityCoordinatesResponse;
import com.vova.laba.dto.weather.WeatherInfoDto;
import com.vova.laba.exceptions.ApiException;
import com.vova.laba.exceptions.BadRequestException;
import java.util.List;
import java.util.Optional;

public interface WeatherApiService {

  public Optional<CityCoordinatesResponse> getCoordinates(String city) throws BadRequestException;

  public Optional<WeatherInfoDto> getWeather(Optional<CityCoordinatesResponse> coordOptional)
      throws BadRequestException, ApiException;

  public Optional<List<WeatherInfoDto>> getFiveDaysWeather(
      Optional<CityCoordinatesResponse> coordOptional) throws BadRequestException, ApiException;

  public void addToDatabase(CityInfoDto city, WeatherInfoDto weather)
      throws BadRequestException, ApiException;

  public void addToDatabase(CityInfoDto city, List<WeatherInfoDto> weathers)
      throws BadRequestException, ApiException;
}
