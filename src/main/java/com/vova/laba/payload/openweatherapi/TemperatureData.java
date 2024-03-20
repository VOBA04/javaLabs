package com.vova.laba.payload.openweatherapi;

import lombok.Data;

@Data
public class TemperatureData {
    private Float temp;
    private Integer pressure;
    private Integer humidity;
}
