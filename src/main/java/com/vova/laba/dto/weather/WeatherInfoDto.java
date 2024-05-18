package com.vova.laba.dto.weather;

import com.vova.laba.dto.openweatherapi.WeatherDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherInfoDto {
  private Float temp;
  private Integer pressure;
  private Integer humidity;
  private Float speed;
  private Integer deg;
  private Integer clouds;
  private WeatherDate date = new WeatherDate();
  private String weatherDescription;
  private String weatherIcon;

  public void setDay(Integer day) {
    date.setDay(day);
  }

  public void setMonth(Integer month) {
    date.setMonth(month);
  }

  public void setYear(Integer year) {
    date.setYear(year);
  }
}