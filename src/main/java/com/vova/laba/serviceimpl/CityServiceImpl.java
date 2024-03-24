package com.vova.laba.serviceimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.vova.laba.DTO.city.CityDispalyWithWeather;
import com.vova.laba.DTO.city.CityDisplayDTO;
import com.vova.laba.DTO.city.CityInfoDTO;
import com.vova.laba.model.City;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.service.CityService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private ModelMapper modelMapper;

    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<List<CityDisplayDTO>> getAllCities() {
        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(modelMapper.map(cities, CityDisplayDTO[].class)));
    }

    @Override
    public Optional<CityDisplayDTO> getCityById(Long id) {
        City city = cityRepository.findById(id).orElse(null);
        if (city == null) {
            return Optional.empty();
        }
        return Optional.of(modelMapper.map(city, CityDisplayDTO.class));
    }

    @Override
    public CityDisplayDTO saveCity(CityInfoDTO city) {
        Optional<City> cityModel = cityRepository.findCityByCityName(city.getCityName());
        if (cityModel.isPresent()) {
            return modelMapper.map(cityModel.get(), CityDisplayDTO.class);
        }
        return modelMapper.map(cityRepository.save(modelMapper.map(city, City.class)), CityDisplayDTO.class);
    }

    @Override
    public CityDisplayDTO updateCity(Long id, CityInfoDTO city) {
        City cityModel = modelMapper.map(city, City.class);
        cityModel.setId(id);
        return modelMapper.map(cityRepository.save(cityModel), CityDisplayDTO.class);
    }

    @Override
    public boolean deleteCity(Long id) {
        City city = cityRepository.findById(id).orElse(null);
        if (city != null) {
            city.getUsers().forEach(user -> user.deleteCity(id));
            cityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CityDispalyWithWeather> getAllCityWeather(Long cityId) {
        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            return Optional.empty();
        }
        return Optional.of(modelMapper.map(city, CityDispalyWithWeather.class));
    }
}
