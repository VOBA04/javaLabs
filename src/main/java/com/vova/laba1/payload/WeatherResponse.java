package com.vova.laba1.payload;

import lombok.Data;

@Data
public class WeatherResponse {
    Float temp;
    Integer pressure;
    Integer humidity;
    Float speed;
    Integer deg;
    Integer clouds;
}