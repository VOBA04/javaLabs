package com.vova.laba.controller;

import java.util.List;

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

import com.vova.laba.DTO.user.UserDisplayDTO;
import com.vova.laba.DTO.user.UserInfoDTO;
import com.vova.laba.service.UserService;

@RestController
@RequestMapping("api/v2/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDisplayDTO>> getAllUsers() {
        return ResponseEntity.of(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDisplayDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @PostMapping("/")
    public ResponseEntity<UserDisplayDTO> createUser(@RequestBody UserInfoDTO user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDisplayDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserInfoDTO user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add_city")
    public ResponseEntity<UserDisplayDTO> addCityToUser(@RequestParam(value = "user_id") Long userId,
            @RequestParam(value = "city_id") Long cityId) {
        return ResponseEntity.of(userService.addCityToUser(userId, cityId));
    }

    @DeleteMapping("/remove_city")
    public ResponseEntity<UserDisplayDTO> removeCityFromUser(@RequestParam(value = "user_id") Long userId,
            @RequestParam(value = "city_id") Long cityId) {
        return ResponseEntity.of(userService.removeCityFromUser(userId, cityId));
    }
}
