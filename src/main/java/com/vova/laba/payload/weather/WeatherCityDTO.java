package com.vova.laba.payload.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherCityDTO {
    private Long id;
    private String name;
}
