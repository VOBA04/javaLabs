package com.vova.laba.payload.city;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityDisplayDTO extends CityInfoDTO {

    private Long id;
}
