package com.vova.laba.dto.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherDisplayDTO extends WeatherInfoDTO {

    private Long id;
    private WeatherCityInfoDTO city = new WeatherCityInfoDTO();
}
