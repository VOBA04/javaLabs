package com.vova.laba.serviceimpl;

import com.vova.laba.aspect.Logging;
import com.vova.laba.dto.user.UserDisplayDto;
import com.vova.laba.dto.user.UserInfoDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.City;
import com.vova.laba.model.User;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.UserRepository;
import com.vova.laba.service.UserService;
import com.vova.laba.utils.cache.GenericCache;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CityRepository cityRepository;

  private ModelMapper modelMapper;

  private GenericCache<Long, User> cache;

  private String userErrorMessage = "There is no user with id=";

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository,
      CityRepository cityrRepository,
      ModelMapper modelMapper,
      GenericCache<Long, User> cache) {
    this.userRepository = userRepository;
    this.cityRepository = cityrRepository;
    this.modelMapper = modelMapper;
    this.cache = cache;
  }

  @Logging
  @Override
  public Optional<List<UserDisplayDto>> getAllUsers() {
    List<User> users = userRepository.findAll();
    return Optional.of(Arrays.asList(modelMapper.map(users, UserDisplayDto[].class)));
  }

  @Logging
  @Override
  public Optional<UserDisplayDto> getUserById(Long id) throws NotFoundExcepcion {
    User user = cache.get(id).orElseGet(() -> userRepository.findById(id).orElse(null));
    if (user == null) {
      throw new NotFoundExcepcion(userErrorMessage, id);
    }
    cache.put(id, user);
    return Optional.of(modelMapper.map(user, UserDisplayDto.class));
  }

  @Logging
  @Override
  public Optional<UserDisplayDto> saveUser(UserInfoDto user) throws BadRequestException {
    if (user.getName() == null || user.getName().equals("")) {
      throw new BadRequestException("Wrong user name");
    }
    try {
      return Optional.of(
          modelMapper.map(
              userRepository.save(modelMapper.map(user, User.class)), UserDisplayDto.class));
    } catch (Exception e) {
      throw new BadRequestException("Wrong user parameters");
    }
  }

  @Logging
  @Override
  public Optional<UserDisplayDto> updateUser(Long id, UserInfoDto user) throws BadRequestException {
    if (user.getName() == null || user.getName().equals("")) {
      throw new BadRequestException("Wrong user name");
    }
    User userModel = modelMapper.map(user, User.class);
    userModel.setId(id);
    Optional<User> userCache = cache.get(id);
    cache.remove(id);
    try {
      return Optional.of(modelMapper.map(userRepository.save(userModel), UserDisplayDto.class));
    } catch (Exception e) {
      if (userCache.isPresent()) {
        cache.put(id, userCache.get());
      }
      throw new BadRequestException("Wrong user parameters");
    }
  }

  @Logging
  @Override
  public Optional<UserDisplayDto> deleteUser(Long id) throws NotFoundExcepcion {
    User user = userRepository.findById(id).orElse(null);
    if (user != null) {
      Set.copyOf(user.getCities()).forEach(city -> city.deleteUser(id));
      userRepository.deleteById(id);
      cache.remove(id);
      return Optional.of(modelMapper.map(user, UserDisplayDto.class));
    }
    throw new NotFoundExcepcion(userErrorMessage, id);
  }

  @Logging
  @Override
  public Optional<UserDisplayDto> addCityToUser(Long userId, Long cityId) throws NotFoundExcepcion {
    User user = cache.get(userId).orElseGet(() -> userRepository.findById(userId).orElse(null));
    City city = cityRepository.findById(cityId).orElse(null);
    if (user == null) {
      throw new NotFoundExcepcion(userErrorMessage, userId);
    }
    if (city == null) {
      throw new NotFoundExcepcion("There is no city with id ", cityId);
    }
    user.addCity(city);
    cache.put(userId, user);
    return Optional.of(modelMapper.map(userRepository.save(user), UserDisplayDto.class));
  }

  @Logging
  @Override
  public Optional<UserDisplayDto> removeCityFromUser(Long userId, Long cityId)
      throws NotFoundExcepcion {
    User user = cache.get(userId).orElseGet(() -> userRepository.findById(userId).orElse(null));
    if (user == null) {
      throw new NotFoundExcepcion(userErrorMessage, cityId);
    }
    cache.put(userId, user);
    user.deleteCity(cityId);
    return Optional.of(modelMapper.map(userRepository.save(user), UserDisplayDto.class));
  }
}
