package com.vova.laba.payload.openweatherapi;

import lombok.Data;

@Data
public class OpenWeatherReport {
    private TemperatureData main;
    private WindData wind;
    private CloudsData clouds;
    private Long dt;
}
