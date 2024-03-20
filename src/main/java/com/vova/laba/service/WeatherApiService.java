package com.vova.laba.service;

import java.util.List;

import com.vova.laba.payload.city.CityInfoDTO;
import com.vova.laba.payload.openweatherapi.CityCoordinatesResponse;
import com.vova.laba.payload.weather.WeatherInfoDTO;

public interface WeatherApiService {

    public WeatherInfoDTO getWeather(CityCoordinatesResponse coord);

    public List<WeatherInfoDTO> getFiveDaysWeather(CityCoordinatesResponse coord);

    public CityCoordinatesResponse getCoordinates(String city);

    public void addToDatabase(CityInfoDTO city, WeatherInfoDTO weather);

    public void addToDatabase(CityInfoDTO city, List<WeatherInfoDTO> weathers);
}
