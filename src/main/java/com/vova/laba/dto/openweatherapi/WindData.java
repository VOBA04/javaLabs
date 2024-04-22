package com.vova.laba.dto.openweatherapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindData {
  private Float speed;
  private Integer deg;
}
