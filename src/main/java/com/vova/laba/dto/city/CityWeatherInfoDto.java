package com.vova.laba.dto.city;

import com.vova.laba.dto.weather.WeatherInfoDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityWeatherInfoDto extends WeatherInfoDto {
  private Long id;
}
