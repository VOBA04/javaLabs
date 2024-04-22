package com.vova.laba.serviceimpl;

import com.vova.laba.aspect.Logging;
import com.vova.laba.dto.city.CityDispalyWithWeather;
import com.vova.laba.dto.city.CityDisplayDto;
import com.vova.laba.dto.city.CityInfoDto;
import com.vova.laba.dto.city.CityWeatherInfoDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.City;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.CityService;
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
public class CityServiceImpl implements CityService {

  private final CityRepository cityRepository;
  private final WeatherRepository weatherRepository;

  private ModelMapper modelMapper;

  private GenericCache<Long, City> cache;

  private String cityErrorMessage = "There is no city with id=";

  @Autowired
  public CityServiceImpl(
      CityRepository cityRepository,
      WeatherRepository weatherRepository,
      ModelMapper modelMapper,
      GenericCache<Long, City> cache) {
    this.cityRepository = cityRepository;
    this.weatherRepository = weatherRepository;
    this.modelMapper = modelMapper;
    this.cache = cache;
  }

  @Logging
  @Override
  public Optional<List<CityDisplayDto>> getAllCities() {
    List<City> cities = cityRepository.findAll();
    return Optional.of(Arrays.asList(modelMapper.map(cities, CityDisplayDto[].class)));
  }

  @Logging
  @Override
  public Optional<CityDisplayDto> getCityById(Long id) throws NotFoundExcepcion {
    City city = cache.get(id).orElseGet(() -> cityRepository.findById(id).orElse(null));
    if (city == null) {
      throw new NotFoundExcepcion(cityErrorMessage, id);
    }
    cache.put(id, city);
    return Optional.of(modelMapper.map(city, CityDisplayDto.class));
  }

  @Logging
  @Override
  public Optional<CityDisplayDto> saveCity(CityInfoDto city) throws BadRequestException {
    if (city.getCityName() == null || city.getCityName().equals("")) {
      throw new BadRequestException("Wrong city name");
    }
    Optional<City> cityModel = cityRepository.findCityByCityName(city.getCityName());
    if (cityModel.isPresent()) {
      return Optional.of(modelMapper.map(cityModel.get(), CityDisplayDto.class));
    }
    return Optional.of(
        modelMapper.map(
            cityRepository.save(modelMapper.map(city, City.class)), CityDisplayDto.class));
  }

  @Logging
  @Override
  public Optional<List<CityDisplayDto>> saveCities(List<CityInfoDto> cities)
      throws BadRequestException {
    if (cities.stream().anyMatch(c -> (c.getCityName() == null || c.getCityName().equals("")))) {
      throw new BadRequestException("Wrong city or cities name");
    }
    return Optional.of(
        cities.stream()
            .map(c -> cityRepository.save(modelMapper.map(c, City.class)))
            .map(c -> modelMapper.map(c, CityDisplayDto.class))
            .toList());
  }

  @Logging
  @Override
  public Optional<CityDisplayDto> updateCity(Long id, CityInfoDto city) throws BadRequestException {
    if (city.getCityName() == null || city.getCityName().equals("")) {
      throw new BadRequestException("Wrong city name");
    }
    City cityModel = modelMapper.map(city, City.class);
    cityModel.setId(id);
    Optional<City> cityCache = cache.get(id);
    cache.remove(id);
    try {
      return Optional.of(modelMapper.map(cityRepository.save(cityModel), CityDisplayDto.class));
    } catch (Exception e) {
      if (cityCache.isPresent()) {
        cache.put(id, cityCache.get());
      }
      throw new BadRequestException("Wrong city parameters");
    }
  }

  @Logging
  @Override
  public Optional<CityDispalyWithWeather> deleteCity(Long id) throws NotFoundExcepcion {
    City city = cityRepository.findById(id).orElse(null);
    if (city != null) {
      Set.copyOf(city.getUsers()).forEach(user -> user.deleteCity(id));
      cityRepository.deleteById(id);
      cache.remove(id);
      return Optional.of(modelMapper.map(city, CityDispalyWithWeather.class));
    }
    throw new NotFoundExcepcion(cityErrorMessage, id);
  }

  @Logging
  @Override
  public Optional<CityDispalyWithWeather> getAllCityWeather(Long cityId) throws NotFoundExcepcion {
    City city = cityRepository.findById(cityId).orElse(null);
    if (city == null) {
      throw new NotFoundExcepcion(cityErrorMessage, cityId);
    }
    return Optional.of(modelMapper.map(city, CityDispalyWithWeather.class));
  }

  @Logging
  @Override
  public Optional<CityDispalyWithWeather> getCityWeatherByTemperature(
      Long cityId, Float minTemp, Float maxTemp) throws NotFoundExcepcion {
    City cityModel = cityRepository.findById(cityId).orElse(null);
    Optional<List<Weather>> weather =
        weatherRepository.findWeatherByCityIdAndTemperature(cityId, minTemp, maxTemp);
    if (cityModel == null) {
      throw new NotFoundExcepcion(cityErrorMessage, cityId);
    }
    CityDispalyWithWeather city = modelMapper.map(cityModel, CityDispalyWithWeather.class);
    if (!weather.isPresent()) {
      throw new NotFoundExcepcion("There is no weather with a set temperature");
    }
    city.setWeather(Arrays.asList(modelMapper.map(weather.get(), CityWeatherInfoDto[].class)));
    return Optional.of(city);
  }
}
