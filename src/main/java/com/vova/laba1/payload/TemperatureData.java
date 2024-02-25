package com.vova.laba1.payload;

import lombok.Data;

@Data
public class TemperatureData {
    Float temp;
    Integer pressure;
    Integer humidity;
}
