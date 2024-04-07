package com.vova.laba.dto.openweatherapi;

import lombok.Data;

@Data
public class OpenWeatherReport {
  private TemperatureData main;
  private WindData wind;
  private CloudsData clouds;
  private Long dt;
}
