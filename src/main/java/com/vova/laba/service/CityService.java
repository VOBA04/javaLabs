package com.vova.laba.service;

import java.util.List;
import java.util.Optional;

import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDTO;
import com.vova.laba.dto.city.CityInfoDTO;

public interface CityService {

    public Optional<List<CityDisplayDTO>> getAllCities();

    public Optional<CityDisplayDTO> getCityById(Long id);

    public CityDisplayDTO saveCity(CityInfoDTO city);

    public CityDisplayDTO updateCity(Long id, CityInfoDTO city);

    public boolean deleteCity(Long id);

    public Optional<CityDispalyWithWeather> getAllCityWeather(Long cityId);

    public Optional<CityDispalyWithWeather> getCityWeatherByTemperature(Long cityId, Float minTemp, Float maxTemp);
}
