package com.vova.laba.model;

import com.vova.laba.dto.openweatherapi.WeatherDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weather")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "temp")
  Float temp;

  @Column(name = "pressure")
  Integer pressure;

  @Column(name = "humidity")
  Integer humidity;

  @Column(name = "speed")
  Float speed;

  @Column(name = "deg")
  Integer deg;

  @Column(name = "clouds")
  Integer clouds;

  @Column(name = "day")
  Integer day;

  @Column(name = "month")
  Integer month;

  @Column(name = "year")
  Integer year;

  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private City city = new City();

  public void setDate(WeatherDate date) {
    day = date.getDay();
    month = date.getMonth();
    year = date.getYear();
  }

  public void setCityId(Long id) {
    city.setId(id);
  }
}
