package com.vova.laba.serviceimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDTO;
import com.vova.laba.dto.city.CityInfoDTO;
import com.vova.laba.dto.city.CityWeatherInfoDTO;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.CityService;
import com.vova.laba.utils.cache.GenericCache;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final WeatherRepository weatherRepository;

    private ModelMapper modelMapper;

    private GenericCache<Long, City> cache;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, WeatherRepository weatherRepository, ModelMapper modelMapper,
            GenericCache<Long, City> cache) {
        this.cityRepository = cityRepository;
        this.weatherRepository = weatherRepository;
        this.modelMapper = modelMapper;
        this.cache = cache;
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
        City city = cache.get(id).orElseGet(() -> cityRepository.findById(id).orElse(null));
        if (city == null) {
            return Optional.empty();
        }
        cache.put(id, city);
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
        cache.remove(id);
        return modelMapper.map(cityRepository.save(cityModel), CityDisplayDTO.class);
    }

    @Override
    public boolean deleteCity(Long id) {
        City city = cityRepository.findById(id).orElse(null);
        if (city != null) {
            city.getUsers().forEach(user -> user.deleteCity(id));
            cityRepository.deleteById(id);
            cache.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CityDispalyWithWeather> getAllCityWeather(Long cityId) {
        return Optional.ofNullable(
                modelMapper.map(cityRepository.findById(cityId).orElse(null), CityDispalyWithWeather.class));
    }

    @Override
    public Optional<CityDispalyWithWeather> getCityWeatherByTemperature(Long cityId, Float minTemp, Float maxTemp) {
        CityDispalyWithWeather city = modelMapper.map(
                cityRepository.findById(cityId).orElse(null),
                CityDispalyWithWeather.class);
        Optional<List<Weather>> weather = weatherRepository.findWeatherByCityIdAndTemperature(cityId, minTemp, maxTemp);
        if (city == null || !weather.isPresent()) {
            return Optional.empty();
        }
        city.setWeather(Arrays.asList(modelMapper.map(weather.get(), CityWeatherInfoDTO[].class)));
        return Optional.of(city);
    }
}
