package com.vova.laba.dto.user;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDisplayDto extends UserInfoDto {
  private Long id;
  private List<UserCityInfoDto> cities;
}
