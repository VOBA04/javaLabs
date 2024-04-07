package com.vova.laba.dto.openweatherapi;

import lombok.Data;

@Data
public class OpenWeatherFiveDaysReport {
  OpenWeatherReport[] list;
}
