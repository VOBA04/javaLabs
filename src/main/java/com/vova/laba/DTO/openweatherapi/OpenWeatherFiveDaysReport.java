package com.vova.laba.DTO.openweatherapi;

import lombok.Data;

@Data
public class OpenWeatherFiveDaysReport {
    OpenWeatherReport[] list;
}
