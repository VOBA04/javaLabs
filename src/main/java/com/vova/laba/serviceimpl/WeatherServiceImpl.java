package com.vova.laba.serviceimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vova.laba.dto.weather.WeatherCreateDTO;
import com.vova.laba.dto.weather.WeatherDisplayDTO;
import com.vova.laba.model.Weather;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.WeatherService;
import com.vova.laba.utils.cache.GenericCache;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    private ModelMapper modelMapper;

    private GenericCache<Long, Weather> cache;

    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository, ModelMapper modelMapper,
            GenericCache<Long, Weather> cache) {
        this.weatherRepository = weatherRepository;
        this.modelMapper = modelMapper;
        this.cache = cache;
    }

    @Override
    public Optional<List<WeatherDisplayDTO>> getAllWeather() {
        List<Weather> weathers = weatherRepository.findAll();
        if (weathers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(modelMapper.map(weathers, WeatherDisplayDTO[].class)));
    }

    @Override
    public Optional<WeatherDisplayDTO> getWeatherById(Long id) {
        Weather weather = cache.get(id).orElseGet(() -> weatherRepository.findById(id).orElse(null));
        if (weather == null) {
            return Optional.empty();
        }
        cache.put(id, weather);
        return Optional.of(modelMapper.map(weather, WeatherDisplayDTO.class));
    }

    @Override
    public WeatherDisplayDTO saveWeather(WeatherCreateDTO weather) {
        return modelMapper.map(weatherRepository.save(modelMapper.map(weather, Weather.class)),
                WeatherDisplayDTO.class);
    }

    @Override
    public WeatherDisplayDTO updateWeather(Long id, WeatherCreateDTO weather) {
        Weather weatherModel = modelMapper.map(weather, Weather.class);
        weatherModel.setId(id);
        cache.remove(id);
        return modelMapper.map(weatherRepository.save(weatherModel), WeatherDisplayDTO.class);
    }

    @Override
    public boolean deleteWeather(Long id) {
        Weather weather = weatherRepository.findById(id).orElse(null);
        if (weather != null) {
            weatherRepository.deleteById(id);
            cache.remove(id);
            return true;
        }
        return false;
    }
}
