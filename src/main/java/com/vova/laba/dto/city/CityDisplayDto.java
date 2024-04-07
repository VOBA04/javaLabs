package com.vova.laba.dto.city;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityDisplayDto extends CityInfoDto {

  private Long id;
}
