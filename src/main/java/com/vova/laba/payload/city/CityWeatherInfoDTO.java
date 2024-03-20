package com.vova.laba.payload.city;

import com.vova.laba.payload.weather.WeatherInfoDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityWeatherInfoDTO extends WeatherInfoDTO {
    private Long id;
}
