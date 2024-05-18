package com.vova.laba.service;

import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import java.util.List;
import java.util.Optional;

public interface WeatherService {

  public Optional<List<WeatherDisplayDto>> getAllWeather();

  public Optional<WeatherDisplayDto> getWeatherById(Long id) throws NotFoundExcepcion;

  public Optional<List<WeatherDisplayDto>> getAllWeatherSorted();

  public Optional<WeatherDisplayDto> saveWeather(WeatherCreateDto weather)
      throws BadRequestException;

  public Optional<List<WeatherDisplayDto>> saveWeathers(List<WeatherCreateDto> forecasts)
      throws BadRequestException;

  public Optional<WeatherDisplayDto> updateWeather(Long id, WeatherCreateDto weather)
      throws BadRequestException;

  public Optional<WeatherDisplayDto> deleteWeather(Long id) throws NotFoundExcepcion;
}
