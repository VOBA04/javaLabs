package com.vova.laba.DTO.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherCityInfoDTO {
    private Long id;
    private String cityName;
}
