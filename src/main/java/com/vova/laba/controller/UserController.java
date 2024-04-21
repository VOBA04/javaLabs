package com.vova.laba.controller;

import com.vova.laba.dto.user.UserDisplayDto;
import com.vova.laba.dto.user.UserInfoDto;
import com.vova.laba.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/user")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserDisplayDto>> getAllUsers() {
    return ResponseEntity.of(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDisplayDto> getUserById(@PathVariable("id") Long id) {
    return ResponseEntity.of(userService.getUserById(id));
  }

  @PostMapping("/")
  public ResponseEntity<UserDisplayDto> createUser(@RequestBody UserInfoDto user) {
    return ResponseEntity.of(userService.saveUser(user));
  }

  @PostMapping("/users")
  public ResponseEntity<List<UserDisplayDto>> createUsers(@RequestBody List<UserInfoDto> users) {
    return ResponseEntity.of(userService.saveUsers(users));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDisplayDto> updateUser(
      @PathVariable("id") Long id, @RequestBody UserInfoDto user) {
    return ResponseEntity.of(userService.updateUser(id, user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<UserDisplayDto> deleteUser(@PathVariable("id") Long id) {
    return ResponseEntity.of(userService.deleteUser(id));
  }

  @PostMapping("/add_city")
  public ResponseEntity<UserDisplayDto> addCityToUser(
      @RequestParam(value = "user_id") Long userId, @RequestParam(value = "city_id") Long cityId) {
    return ResponseEntity.of(userService.addCityToUser(userId, cityId));
  }

  @DeleteMapping("/remove_city")
  public ResponseEntity<UserDisplayDto> removeCityFromUser(
      @RequestParam(value = "user_id") Long userId, @RequestParam(value = "city_id") Long cityId) {
    return ResponseEntity.of(userService.removeCityFromUser(userId, cityId));
  }
}
