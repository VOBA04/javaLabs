package com.vova.laba.serviceimpl;

import com.vova.laba.aspect.annotation.Logging;
import com.vova.laba.dto.weather.WeatherCreateDto;
import com.vova.laba.dto.weather.WeatherDisplayDto;
import com.vova.laba.exceptions.BadRequestException;
import com.vova.laba.exceptions.NotFoundExcepcion;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.CityRepository;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.WeatherService;
import com.vova.laba.utils.cache.GenericCache;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {

  private final WeatherRepository weatherRepository;
  private final CityRepository cityRepository;

  private ModelMapper modelMapper;

  private GenericCache<Long, Weather> cache;

  private String weatherErrorMessage = "There is no weather with id=";

  public WeatherServiceImpl(
      WeatherRepository weatherRepository,
      ModelMapper modelMapper,
      GenericCache<Long, Weather> cache,
      CityRepository cityRepository) {
    this.weatherRepository = weatherRepository;
    this.modelMapper = modelMapper;
    this.cache = cache;
    this.cityRepository = cityRepository;
  }

  @Logging
  @Override
  public Optional<List<WeatherDisplayDto>> getAllWeather() {
    List<Weather> weathers = weatherRepository.findAll();
    return Optional.of(Arrays.asList(modelMapper.map(weathers, WeatherDisplayDto[].class)));
  }

  @Logging
  @Override
  public Optional<WeatherDisplayDto> getWeatherById(Long id) throws NotFoundExcepcion {
    Weather weather = cache.get(id).orElseGet(() -> weatherRepository.findById(id).orElse(null));
    if (weather == null) {
      throw new NotFoundExcepcion(weatherErrorMessage, id);
    }
    cache.put(id, weather);
    return Optional.of(modelMapper.map(weather, WeatherDisplayDto.class));
  }

  @Logging
  @Override
  public Optional<List<WeatherDisplayDto>> getAllWeatherSorted() {
    List<Weather> weathers = weatherRepository.findAllSorted().orElse(new ArrayList<>());
    return Optional.of(Arrays.asList(modelMapper.map(weathers, WeatherDisplayDto[].class)));
  }

  @Logging
  @Override
  public Optional<WeatherDisplayDto> saveWeather(WeatherCreateDto weather)
      throws BadRequestException {
    if (weather.getCityId() == null
        || weather.getTemp() == null
        || weather.getPressure() == null
        || weather.getHumidity() == 0
        || weather.getSpeed() == null
        || weather.getDeg() == null
        || weather.getClouds() == null
        || !cityRepository.findById(weather.getCityId()).isPresent()
        || !(weather.getTemp() > -273
            && weather.getTemp() < 100
            && weather.getPressure() > 0
            && weather.getHumidity() >= 0
            && weather.getHumidity() <= 100
            && weather.getSpeed() >= 0
            && weather.getSpeed() <= 120
            && weather.getDeg() >= 0
            && weather.getDeg() <= 359
            && weather.getClouds() >= 0
            && weather.getClouds() <= 100)) {
      throw new BadRequestException("Wrong weather parameters");
    }
    return Optional.of(
        modelMapper.map(
            weatherRepository.save(modelMapper.map(weather, Weather.class)),
            WeatherDisplayDto.class));
  }

  @Logging
  @Override
  public Optional<List<WeatherDisplayDto>> saveWeathers(List<WeatherCreateDto> forecasts)
      throws BadRequestException {
    if (forecasts.stream().noneMatch(w -> cityRepository.findById(w.getCityId()).isPresent())) {
      throw new BadRequestException("Wrong weather forecast(s) parameters");
    }
    return Optional.of(
        forecasts.stream()
            .map(w -> weatherRepository.save(modelMapper.map(w, Weather.class)))
            .map(w -> modelMapper.map(w, WeatherDisplayDto.class))
            .toList());
  }

  @Logging
  @Override
  public Optional<WeatherDisplayDto> updateWeather(Long id, WeatherCreateDto weather)
      throws BadRequestException {
    if (weather.getCityId() == null
        || weather.getTemp() == null
        || weather.getPressure() == null
        || weather.getHumidity() == 0
        || weather.getSpeed() == null
        || weather.getDeg() == null
        || weather.getClouds() == null
        || !cityRepository.findById(weather.getCityId()).isPresent()
        || !(weather.getTemp() > -273
            && weather.getTemp() < 100
            && weather.getPressure() > 0
            && weather.getHumidity() >= 0
            && weather.getHumidity() <= 100
            && weather.getSpeed() >= 0
            && weather.getSpeed() <= 120
            && weather.getDeg() >= 0
            && weather.getDeg() <= 359
            && weather.getClouds() >= 0
            && weather.getClouds() <= 100)) {
      throw new BadRequestException("Wrong weather parameters");
    }
    Weather weatherModel = modelMapper.map(weather, Weather.class);
    weatherModel.setId(id);
    cache.remove(id);
    return Optional.of(
        modelMapper.map(weatherRepository.save(weatherModel), WeatherDisplayDto.class));
  }

  @Logging
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
