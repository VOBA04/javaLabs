package com.vova.laba.dto.city;

import com.vova.laba.dto.weather.WeatherInfoDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityWeatherInfoDTO extends WeatherInfoDTO {
    private Long id;
}
