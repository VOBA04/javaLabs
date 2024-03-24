package com.vova.laba.service;

import java.util.List;
import java.util.Optional;

import com.vova.laba.DTO.user.UserDisplayDTO;
import com.vova.laba.DTO.user.UserInfoDTO;

public interface UserService {

    public Optional<List<UserDisplayDTO>> getAllUsers();

    public Optional<UserDisplayDTO> getUserById(Long id);

    public UserDisplayDTO saveUser(UserInfoDTO user);

    public UserDisplayDTO updateUser(Long id, UserInfoDTO user);

    public boolean deleteUser(Long id);

    public Optional<UserDisplayDTO> addCityToUser(Long userId, Long cityId);

    public Optional<UserDisplayDTO> removeCityFromUser(Long userId, Long cityId);
}
