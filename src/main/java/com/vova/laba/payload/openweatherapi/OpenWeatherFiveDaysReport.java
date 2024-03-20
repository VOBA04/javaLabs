package com.vova.laba.payload.openweatherapi;

import lombok.Data;

@Data
public class OpenWeatherFiveDaysReport {
    OpenWeatherReport[] list;
}
