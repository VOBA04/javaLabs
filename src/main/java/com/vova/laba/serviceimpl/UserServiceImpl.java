package com.vova.laba.serviceimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vova.laba.dto.user.UserDisplayDTO;
import com.vova.laba.dto.user.UserInfoDTO;
import com.vova.laba.model.City;
import com.vova.laba.model.User;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.UserRepository;
import com.vova.laba.service.UserService;
import com.vova.laba.utils.cache.GenericCache;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    private ModelMapper modelMapper;

    private GenericCache<Long, User> cache;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CityRepository cityrRepository, ModelMapper modelMapper,
            GenericCache<Long, User> cache) {
        this.userRepository = userRepository;
        this.cityRepository = cityrRepository;
        this.modelMapper = modelMapper;
        this.cache = cache;
    }

    @Override
    public Optional<List<UserDisplayDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(modelMapper.map(users, UserDisplayDTO[].class)));
    }

    @Override
    public Optional<UserDisplayDTO> getUserById(Long id) {
        User user = cache.get(id).orElseGet(() -> userRepository.findById(id).orElse(null));
        if (user == null) {
            return Optional.empty();
        }
        cache.put(id, user);
        return Optional.of(modelMapper.map(user, UserDisplayDTO.class));
    }

    @Override
    public UserDisplayDTO saveUser(UserInfoDTO user) {
        return modelMapper.map(userRepository.save(modelMapper.map(user, User.class)), UserDisplayDTO.class);
    }

    @Override
    public UserDisplayDTO updateUser(Long id, UserInfoDTO user) {
        User userModel = modelMapper.map(user, User.class);
        userModel.setId(id);
        cache.remove(id);
        return modelMapper.map(userRepository.save(userModel), UserDisplayDTO.class);
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Set.copyOf(user.getCities()).forEach(city -> city.deleteUser(id));
            userRepository.deleteById(id);
            cache.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserDisplayDTO> addCityToUser(Long userId, Long cityId) {
        User user = cache.get(userId).orElseGet(() -> userRepository.findById(userId).orElse(null));
        City city = cityRepository.findById(cityId).orElse(null);
        if (user != null && city != null) {
            user.addCity(city);
            cache.put(userId, user);
            return Optional.of(modelMapper.map(userRepository.save(user), UserDisplayDTO.class));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDisplayDTO> removeCityFromUser(Long userId, Long cityId) {
        User user = cache.get(userId).orElseGet(() -> userRepository.findById(userId).orElse(null));
        if (user != null) {
            cache.put(userId, user);
            user.deleteCity(cityId);
            return Optional.of(modelMapper.map(userRepository.save(user), UserDisplayDTO.class));
        }
        return Optional.empty();
    }
}
