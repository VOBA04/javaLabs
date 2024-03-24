package com.vova.laba.dto.user;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDisplayDTO extends UserInfoDTO {

    private Long id;

    private List<UserCityInfoDTO> cities;
}
