package com.vova.laba1.payload;

import lombok.Data;

@Data
public class OpenWeatherReport {
    TemperatureData main;
    WindData wind;
    CloudsData clouds;
}
