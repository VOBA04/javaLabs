package com.vova.laba.dto.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeatherCreateDTO extends WeatherInfoDTO {

    private Long cityId;
}
