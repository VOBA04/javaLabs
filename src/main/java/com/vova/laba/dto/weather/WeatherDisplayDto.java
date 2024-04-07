package com.vova.laba.dto.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherDisplayDto extends WeatherInfoDto {
  private Long id;
  private WeatherCityInfoDto city = new WeatherCityInfoDto();
}
