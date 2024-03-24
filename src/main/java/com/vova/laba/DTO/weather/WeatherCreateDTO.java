package com.vova.laba.DTO.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherCreateDTO extends WeatherInfoDTO {

    private Long cityId;
}
