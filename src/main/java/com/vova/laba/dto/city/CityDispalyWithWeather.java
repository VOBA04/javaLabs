package com.vova.laba.dto.city;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityDispalyWithWeather extends CityDisplayDTO {
    List<CityWeatherInfoDTO> weather;
}
