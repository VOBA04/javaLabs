package com.vova.laba.payload.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherDisplayDTO extends WeatherInfoDTO {

    private Long id;
    private WeatherCityDTO city = new WeatherCityDTO();
}
