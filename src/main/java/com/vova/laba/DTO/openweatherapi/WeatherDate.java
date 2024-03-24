package com.vova.laba.DTO.openweatherapi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherDate {
    private Integer day;
    private Integer month;
    private Integer year;

    public WeatherDate(Integer day, Integer month, Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setDate(String dateInString) {
        String yearInString = dateInString.substring(0, 3);
        String monthInString = dateInString.substring(5, 6);
        String dayInString = dateInString.substring(8, 9);
        day = Integer.valueOf(dayInString);
        month = Integer.valueOf(monthInString);
        year = Integer.valueOf(yearInString);
    }

    public void setDate(Integer day, Integer month, Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static WeatherDate stringToDate(String dateInString) {
        String yearInString = dateInString.substring(0, 4);
        String monthInString = dateInString.substring(5, 7);
        String dayInString = dateInString.substring(8, 10);
        return new WeatherDate(Integer.valueOf(dayInString), Integer.valueOf(monthInString),
                Integer.valueOf(yearInString));
    }
}
