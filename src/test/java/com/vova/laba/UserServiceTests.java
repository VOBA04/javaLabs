package com.vova.laba;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vova.laba.dto.user.UserDisplayDto;
import com.vova.laba.dto.user.UserInfoDto;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.City;
import com.vova.laba.model.User;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.UserRepository;
import com.vova.laba.serviceimpl.UserServiceImpl;
import com.vova.laba.utils.cache.GenericCache;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

  @Mock private UserRepository userRepository;
  @Mock private CityRepository cityRepository;
  @Mock private GenericCache<Long, User> cache;

  @InjectMocks private UserServiceImpl userService;

  private User user;
  private City city;

  @Spy private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    user = new User();
    user.setId(1L);
    user.setName("Test User");

    city = new City();
    city.setId(1L);
    city.setCityName("Test City");
  }

  @Test
  void testGetUserById() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    Optional<UserDisplayDto> userOptional = userService.getUserById(1L);

    assertTrue(userOptional.isPresent());
    assertEquals(user.getId(), userOptional.get().getId());
    verify(cache, times(1)).put(1L, user);
  }

  @Test
  void testGetUserById_CacheHit() {
    when(cache.get(1L)).thenReturn(Optional.of(user));

    Optional<UserDisplayDto> userOptional = userService.getUserById(1L);

    assertTrue(userOptional.isPresent());
    assertEquals(user.getId(), userOptional.get().getId());
    verify(userRepository, never()).findById(anyLong());
    verify(cache, times(1)).put(1L, user);
  }

  @Test
  void testGetUserById_CacheMiss() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    Optional<UserDisplayDto> userOptional = userService.getUserById(1L);

    assertTrue(userOptional.isPresent());
    assertEquals(user.getId(), userOptional.get().getId());
    verify(cache).put(1L, user);
  }

  @Test
  void testGetUserById_NotFound() {
    when(cache.get(1L)).thenReturn(Optional.empty());
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundExcepcion.class, () -> userService.getUserById(1L));
    verify(cache, never()).put(anyLong(), any(User.class));
  }

  @Test
  void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Arrays.asList(user, user, user));

    List<User> result = Arrays.asList(modelMapper.map(userService.getAllUsers(), User[].class));

    assertEquals(3, result.size());
    assertEquals(user, result.get(1));
  }

  @Test
  void testSaveUser() {
    when(userRepository.save(any(User.class))).thenReturn(user);

    User saved =
        modelMapper.map(
            userService.saveUser(modelMapper.map(user, UserInfoDto.class)).get(), User.class);

    assertNotNull(saved);
    assertEquals(user, saved);
  }

  @Test
  void testSaveUsers() {
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserInfoDto userInfo = modelMapper.map(user, UserInfoDto.class);
    List<UserInfoDto> users = Arrays.asList(userInfo, userInfo, userInfo);
    Optional<List<UserDisplayDto>> result = userService.saveUsers(users);

    assertTrue(result.isPresent());
    assertFalse(result.get().isEmpty());
    assertEquals(modelMapper.map(user, UserDisplayDto.class), result.get().get(1));
    verify(userRepository, times(3)).save(any(User.class));
  }

  @Test
  void testUpdateUser() {
    when(userRepository.save(any(User.class))).thenReturn(user);

    User updated =
        modelMapper.map(
            userService.updateUser(user.getId(), modelMapper.map(user, UserInfoDto.class)),
            User.class);

    assertNotNull(updated);
    assertEquals(user, updated);
    verify(cache, times(1)).remove(1L);
  }

  @Test
  void testDeleteUser() {
    doNothing().when(userRepository).deleteById(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    User result = modelMapper.map(userService.deleteUser(1L), User.class);

    assertEquals(user, result);
    verify(userRepository, times(1)).deleteById(1L);
    verify(cache, times(1)).remove(1L);
  }

  @Test
  void testAddCityToUser() {
    when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(cache.get(1L)).thenReturn(Optional.empty());
    user.addCity(city);
    when(userRepository.save(any(User.class))).thenReturn(user);

    User result =
        modelMapper.map(userService.addCityToUser(city.getId(), user.getId()), User.class);

    assertEquals(user, result);
    verify(cache, times(1)).put(1L, user);

    user.deleteCity(city.getId());
  }

  @Test
  void testRemoveCityFromUser() {
    when(userRepository.save(any(User.class))).thenReturn(user);
    user.addCity(city);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(cache.get(1L)).thenReturn(Optional.empty());

    User result =
        modelMapper.map(userService.removeCityFromUser(user.getId(), city.getId()), User.class);
    user.deleteCity(city.getId());

    assertEquals(user, result);
    verify(cache, times(1)).put(1L, user);
  }
}