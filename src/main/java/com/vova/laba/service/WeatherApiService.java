package com.vova.laba.service;

import java.util.List;
import java.util.Optional;

import com.vova.laba.dto.city.CityInfoDTO;
import com.vova.laba.dto.openweatherapi.CityCoordinatesResponse;
import com.vova.laba.dto.weather.WeatherInfoDTO;

public interface WeatherApiService {

    public Optional<WeatherInfoDTO> getWeather(CityCoordinatesResponse coord);

    public Optional<List<WeatherInfoDTO>> getFiveDaysWeather(CityCoordinatesResponse coord);

    public CityCoordinatesResponse getCoordinates(String city);

    public void addToDatabase(CityInfoDTO city, WeatherInfoDTO weather);

    public void addToDatabase(CityInfoDTO city, List<WeatherInfoDTO> weathers);
}
