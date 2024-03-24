package com.vova.laba.DTO.city;

import com.vova.laba.DTO.weather.WeatherInfoDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityWeatherInfoDTO extends WeatherInfoDTO {
    private Long id;
}
