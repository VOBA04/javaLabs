package com.vova.laba1.service;

import com.vova.laba1.payload.CityCoordinatesResponse;
import com.vova.laba1.payload.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeather(CityCoordinatesResponse coord);

    CityCoordinatesResponse getCoordinates(String city);
}
