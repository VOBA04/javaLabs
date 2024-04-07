package com.vova.laba.dto.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherCityInfoDto {
  private Long id;
  private String cityName;
}
