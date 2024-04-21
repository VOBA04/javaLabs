package com.vova.laba.service;

import com.vova.laba.dto.user.UserDisplayDto;
import com.vova.laba.dto.user.UserInfoDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import java.util.List;
import java.util.Optional;

public interface UserService {

  public Optional<List<UserDisplayDto>> getAllUsers();

  public Optional<UserDisplayDto> getUserById(Long id) throws NotFoundExcepcion;

  public Optional<UserDisplayDto> saveUser(UserInfoDto user) throws BadRequestException;

  public Optional<List<UserDisplayDto>> saveUsers(List<UserInfoDto> users)
      throws BadRequestException;

  public Optional<UserDisplayDto> updateUser(Long id, UserInfoDto user) throws BadRequestException;

  public Optional<UserDisplayDto> deleteUser(Long id) throws NotFoundExcepcion;

  public Optional<UserDisplayDto> addCityToUser(Long userId, Long cityId) throws NotFoundExcepcion;

  public Optional<UserDisplayDto> removeCityFromUser(Long userId, Long cityId)
      throws NotFoundExcepcion;
}
