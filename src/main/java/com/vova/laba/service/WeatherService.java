package com.vova.laba.service;

import java.util.List;
import java.util.Optional;

import com.vova.laba.DTO.weather.WeatherCreateDTO;
import com.vova.laba.DTO.weather.WeatherDisplayDTO;

public interface WeatherService {

    public Optional<List<WeatherDisplayDTO>> getAllWeather();

    public Optional<WeatherDisplayDTO> getWeatherById(Long id);

    public WeatherDisplayDTO saveWeather(WeatherCreateDTO weather);

    public WeatherDisplayDTO updateWeather(Long id, WeatherCreateDTO weather);

    public boolean deleteWeather(Long id);
}
