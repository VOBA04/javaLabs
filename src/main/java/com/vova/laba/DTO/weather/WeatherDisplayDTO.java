package com.vova.laba.DTO.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherDisplayDTO extends WeatherInfoDTO {

    private Long id;
    private WeatherCityInfoDTO city = new WeatherCityInfoDTO();
}
