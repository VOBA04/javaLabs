package com.vova.laba.dto.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherCreateDto extends WeatherInfoDto {
  private Long cityId;
}
