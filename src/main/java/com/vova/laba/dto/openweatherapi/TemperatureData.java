package com.vova.laba.dto.openweatherapi;

import lombok.Data;

@Data
public class TemperatureData {
    private Float temp;
    private Integer pressure;
    private Integer humidity;
}
