package com.vova.laba.serviceimpl;

import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.WeatherService;
import com.vova.laba.utils.cache.GenericCache;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {

  private final WeatherRepository weatherRepository;

  private ModelMapper modelMapper;

  private GenericCache<Long, Weather> cache;

  private String weatherErrorMessage = "There is no weather with id=";

  @Autowired
  public WeatherServiceImpl(
      WeatherRepository weatherRepository,
      ModelMapper modelMapper,
      GenericCache<Long, Weather> cache) {
    this.weatherRepository = weatherRepository;
    this.modelMapper = modelMapper;
    this.cache = cache;
  }

  @Override
  public Optional<List<WeatherDisplayDto>> getAllWeather() {
    List<Weather> weathers = weatherRepository.findAll();
    return Optional.of(Arrays.asList(modelMapper.map(weathers, WeatherDisplayDto[].class)));
  }

  @Override
  public Optional<WeatherDisplayDto> getWeatherById(Long id) throws NotFoundExcepcion {
    Weather weather = cache.get(id).orElseGet(() -> weatherRepository.findById(id).orElse(null));
    if (weather == null) {
      throw new NotFoundExcepcion(weatherErrorMessage, id);
    }
    cache.put(id, weather);
    return Optional.of(modelMapper.map(weather, WeatherDisplayDto.class));
  }

  @Override
  public Optional<WeatherDisplayDto> saveWeather(WeatherCreateDto weather)
      throws BadRequestException {
    try {
      return Optional.of(
          modelMapper.map(
              weatherRepository.save(modelMapper.map(weather, Weather.class)),
              WeatherDisplayDto.class));
    } catch (Exception e) {
      throw new BadRequestException("Wrong weather parameters");
    }
  }

  @Override
  public Optional<WeatherDisplayDto> updateWeather(Long id, WeatherCreateDto weather)
      throws BadRequestException {
    Weather weatherModel = modelMapper.map(weather, Weather.class);
    weatherModel.setId(id);
    Optional<Weather> weatherCache = cache.get(id);
    cache.remove(id);
    try {
      return Optional.of(
          modelMapper.map(weatherRepository.save(weatherModel), WeatherDisplayDto.class));
    } catch (Exception e) {
      if (weatherCache.isPresent()) {
        cache.put(id, weatherCache.get());
      }
      throw new BadRequestException("Wrong weather parameters");
    }
  }

  @Override
  public Optional<WeatherDisplayDto> deleteWeather(Long id) throws NotFoundExcepcion {
    Weather weather = weatherRepository.findById(id).orElse(null);
    if (weather != null) {
      weatherRepository.deleteById(id);
      cache.remove(id);
      return Optional.of(modelMapper.map(weather, WeatherDisplayDto.class));
    }
    throw new NotFoundExcepcion(weatherErrorMessage, id);
  }
}
