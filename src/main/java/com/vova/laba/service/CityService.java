package com.vova.laba.service;

import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import java.util.List;
import java.util.Optional;

public interface CityService {

  public Optional<List<CityDisplayDto>> getAllCities();

  public Optional<CityDisplayDto> getCityById(Long id) throws NotFoundExcepcion;

  public Optional<CityDisplayDto> saveCity(CityInfoDto city) throws BadRequestException;

  public Optional<List<CityDisplayDto>> saveCities(List<CityInfoDto> cities)
      throws BadRequestException;

  public Optional<CityDisplayDto> updateCity(Long id, CityInfoDto city) throws BadRequestException;

  public Optional<CityDispalyWithWeather> deleteCity(Long id) throws NotFoundExcepcion;

  public Optional<CityDispalyWithWeather> getAllCityWeather(Long cityId) throws NotFoundExcepcion;

  public Optional<CityDispalyWithWeather> getCityWeatherByTemperature(
      Long cityId, Float minTemp, Float maxTemp) throws NotFoundExcepcion;
}
