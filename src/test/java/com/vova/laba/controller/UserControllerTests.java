package com.vova.laba.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.user.UserDisplayDto;
import com.vova.laba.serviceimpl.UserServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

  @Mock UserServiceImpl userService;

  @InjectMocks UserController userController;

  private UserDisplayDto user = new UserDisplayDto();

  @BeforeEach
  public void setUp() {
    user.setId(1L);
    user.setName("Test User");
  }

  @Test
  void testGetAllUsers() {
    when(userService.getAllUsers()).thenReturn(Optional.of(Arrays.asList(user, user, user)));

    ResponseEntity<List<UserDisplayDto>> result = userController.getAllUsers();

    assertEquals(3, result.getBody().size());
    assertEquals(user, result.getBody().get(1));
  }

  @Test
  void testGetUserById() {
    when(userService.getUserById(1L)).thenReturn(Optional.of(user));

    ResponseEntity<UserDisplayDto> result = userController.getUserById(1L);

    assertEquals(user, result.getBody());
  }

  @Test
  void testSaveUser() {
    when(userService.saveUser(user)).thenReturn(Optional.of(user));

    ResponseEntity<UserDisplayDto> result = userController.createUser(user);

    assertEquals(user, result.getBody());
  }

  @Test
  void testSaveUsers() {
    when(userService.saveUsers(Arrays.asList(user, user, user)))
        .thenReturn(Optional.of(Arrays.asList(user, user, user)));

    ResponseEntity<List<UserDisplayDto>> result =
        userController.createUsers(Arrays.asList(user, user, user));

    assertEquals(3, result.getBody().size());
    assertEquals(user, result.getBody().get(1));
  }

  @Test
  void testUpdateUser() {
    when(userService.updateUser(1L, user)).thenReturn(Optional.of(user));

    ResponseEntity<UserDisplayDto> result = userController.updateUser(1L, user);

    assertEquals(user, result.getBody());
  }

  @Test
  void testDeleteUser() {
    when(userService.deleteUser(1L)).thenReturn(Optional.of(user));

    ResponseEntity<UserDisplayDto> result = userController.deleteUser(1L);

    assertEquals(user, result.getBody());
  }

  @Test
  void testAddCityToUser() {
    when(userService.addCityToUser(1L, 1L)).thenReturn(Optional.of(user));

    ResponseEntity<UserDisplayDto> result = userController.addCityToUser(1L, 1L);

    assertEquals(user, result.getBody());
  }

  @Test
  void testRemoveCityFromUser() {
    when(userService.removeCityFromUser(1L, 1L)).thenReturn(Optional.of(user));

    ResponseEntity<UserDisplayDto> result = userController.removeCityFromUser(1L, 1L);

    assertEquals(user, result.getBody());
  }
}
