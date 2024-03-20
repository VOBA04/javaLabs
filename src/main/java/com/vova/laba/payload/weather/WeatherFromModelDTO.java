package com.vova.laba.payload.weather;

import lombok.Data;

@Data
public class WeatherFromModelDTO {

    private Float temp;
    private Integer pressure;
    private Integer humidity;
    private Float speed;
    private Integer deg;
    private Integer clouds;
    private Integer day;
    private Integer month;
    private Integer year;
    private Long id;
    private String city;
}
