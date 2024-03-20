package com.vova.laba.serviceimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.vova.laba.model.Weather;
import com.vova.laba.payload.weather.WeatherCreateDTO;
import com.vova.laba.payload.weather.WeatherDisplayDTO;
import com.vova.laba.repository.WeatherRepository;
import com.vova.laba.service.WeatherService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    private ModelMapper modelMapper;

    public WeatherServiceImpl(WeatherRepository weatherRepository, ModelMapper modelMapper) {
        this.weatherRepository = weatherRepository;
        this.modelMapper = modelMapper;
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
        Weather weather = weatherRepository.findById(id).orElse(null);
        if (weather == null) {
            return Optional.empty();
        }
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
        return modelMapper.map(weatherRepository.save(weatherModel), WeatherDisplayDTO.class);
    }

    @Override
    public boolean deleteWeather(Long id) {
        Weather weather = weatherRepository.findById(id).orElse(null);
        if (weather != null) {
            weatherRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
