package com.vova.laba;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.user.UserInfoDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.City;
import com.vova.laba.model.User;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.UserRepository;
import com.vova.laba.service.UserService;
import com.vova.laba.serviceimpl.UserServiceImpl;
import com.vova.laba.utils.cache.SimpleCache;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTests {

  @MockBean private UserRepository userRepository;
  @MockBean private CityRepository cityRepository;

  private UserService userService;
  private User user;
  private City city;
  private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    userService =
        new UserServiceImpl(userRepository, cityRepository, modelMapper, new SimpleCache<>());
    user = new User();
    user.setId(1L);
    user.setName("Test User");

    city = new City();
    city.setId(1L);
    city.setCityName("Test City");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
  }

  @Test
  void testGetUserById() throws NotFoundExcepcion {
    User found = modelMapper.map(userService.getUserById(1L).get(), User.class);
    assertEquals(found.getName(), user.getName());
  }

  @Test
  void testSaveUser() throws BadRequestException {
    when(userRepository.save(any(User.class))).thenReturn(user);
    User saved =
        modelMapper.map(
            userService.saveUser(modelMapper.map(user, UserInfoDto.class)).get(), User.class);
    assertEquals(saved.getName(), user.getName());
  }

  @Test
  void testUpdateUser() throws BadRequestException {
    user.setName("Updated User");
    when(userRepository.save(any(User.class))).thenReturn(user);
    User updated =
        modelMapper.map(
            userService.updateUser(user.getId(), modelMapper.map(user, UserInfoDto.class)),
            User.class);
    assertEquals(updated.getName(), user.getName());
  }

  @Test
  void testDeleteUser() throws NotFoundExcepcion {
    doNothing().when(userRepository).deleteById(1L);
    userService.deleteUser(1L);
    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  void testAddCityToUser() throws NotFoundExcepcion {
    user.getCities().add(city);
    when(userRepository.save(any(User.class))).thenReturn(user);
    User updated = modelMapper.map(userService.addCityToUser(1L, 1L), User.class);
    assertTrue(updated.getCities().contains(city));
  }

  @Test
  void testRemoveCityFromUser() throws NotFoundExcepcion {
    user.getCities().add(city);
    when(userRepository.save(any(User.class))).thenReturn(user);
    User updated = modelMapper.map(userService.removeCityFromUser(1L, 1L), User.class);
    assertFalse(updated.getCities().contains(city));
  }
}