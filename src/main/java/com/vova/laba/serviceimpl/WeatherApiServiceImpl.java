package com.vova.laba.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vova.laba.payload.city.CityDisplayDTO;
import com.vova.laba.payload.city.CityInfoDTO;
import com.vova.laba.payload.openweatherapi.CityCoordinatesResponse;
import com.vova.laba.payload.openweatherapi.OpenWeatherFiveDaysReport;
import com.vova.laba.payload.openweatherapi.OpenWeatherReport;
import com.vova.laba.payload.openweatherapi.WeatherDate;
import com.vova.laba.payload.weather.WeatherCreateDTO;
import com.vova.laba.payload.weather.WeatherInfoDTO;
import com.vova.laba.service.CityService;
import com.vova.laba.service.WeatherApiService;
import com.vova.laba.service.WeatherService;

import jakarta.transaction.Transactional;

@Service
public class WeatherApiServiceImpl implements WeatherApiService {
    @Value("${weatherservice.key}")
    private String key;

    private final CityService cityService;
    private final WeatherService weatherService;
    private final ModelMapper modelMapper;

    public WeatherApiServiceImpl(CityService cityService, WeatherService weatherService, ModelMapper modelMapper) {
        this.cityService = cityService;
        this.weatherService = weatherService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CityCoordinatesResponse getCoordinates(String city) {
        String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={key}";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("city", city);
        uriVariables.put("key", key);
        ResponseEntity<CityCoordinatesResponse[]> responseEntity = restTemplate.getForEntity(apiUrl,
                CityCoordinatesResponse[].class, uriVariables);
        CityCoordinatesResponse[] responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.length > 0) {
            return responseBody[0];
        } else {
            // Обработка случая, когда ответ от API пустой или null
            return null;
        }
    }

    @Override
    public WeatherInfoDTO getWeather(CityCoordinatesResponse coord) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}&units=metric";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("lat", coord.getLat().toString());
        uriVariables.put("lon", coord.getLon().toString());
        uriVariables.put("key", key);
        ResponseEntity<OpenWeatherReport> responseEntity = restTemplate.getForEntity(apiUrl,
                OpenWeatherReport.class, uriVariables);
        OpenWeatherReport responseBody = responseEntity.getBody();
        if (responseBody != null) {
            WeatherInfoDTO weatherResponse = new WeatherInfoDTO();
            weatherResponse.setTemp(responseBody.getMain().getTemp());
            weatherResponse.setHumidity(responseBody.getMain().getHumidity());
            weatherResponse.setPressure(responseBody.getMain().getPressure());
            weatherResponse.setSpeed(responseBody.getWind().getSpeed());
            weatherResponse.setDeg(responseBody.getWind().getDeg());
            weatherResponse.setClouds(responseBody.getClouds().getAll());
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(responseBody.getDt() * 1000));
            weatherResponse.setDate(WeatherDate.stringToDate(date));
            return weatherResponse;
        } else {
            return null;
        }
    }

    @Override
    public List<WeatherInfoDTO> getFiveDaysWeather(CityCoordinatesResponse coord) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={key}&units=metric";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("lat", coord.getLat().toString());
        uriVariables.put("lon", coord.getLon().toString());
        uriVariables.put("key", key);
        ResponseEntity<OpenWeatherFiveDaysReport> responseEntity = restTemplate.getForEntity(apiUrl,
                OpenWeatherFiveDaysReport.class, uriVariables);
        OpenWeatherFiveDaysReport responseBody = responseEntity.getBody();
        if (responseBody != null) {
            OpenWeatherReport[] responseList = responseBody.getList();
            if (responseList.length > 0) {
                List<WeatherInfoDTO> weatherResponses = new ArrayList<>();
                for (int i = 0; i < 40; i += 8) {
                    WeatherInfoDTO weatherResponse = new WeatherInfoDTO();
                    weatherResponse.setTemp(responseList[i].getMain().getTemp());
                    weatherResponse.setHumidity(responseList[i].getMain().getHumidity());
                    weatherResponse.setPressure(responseList[i].getMain().getPressure());
                    weatherResponse.setSpeed(responseList[i].getWind().getSpeed());
                    weatherResponse.setDeg(responseList[i].getWind().getDeg());
                    weatherResponse.setClouds(responseList[i].getClouds().getAll());
                    String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                            .format(new Date(responseList[i].getDt() * 1000));
                    weatherResponse.setDate(WeatherDate.stringToDate(date));
                    weatherResponses.add(weatherResponse);
                }
                return weatherResponses;
            }
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void addToDatabase(CityInfoDTO city, WeatherInfoDTO weather) {
        CityDisplayDTO cityModel = cityService.saveCity(city);
        WeatherCreateDTO weatherDto = modelMapper.map(weather, WeatherCreateDTO.class);
        weatherDto.setCityId(cityModel.getId());
        weatherService.saveWeather(weatherDto);
    }

    @Override
    @Transactional
    public void addToDatabase(CityInfoDTO city, List<WeatherInfoDTO> weathers) {
        CityDisplayDTO cityModel = cityService.saveCity(city);
        for (int i = 0; i < weathers.size(); i++) {
            WeatherCreateDTO weatherDto;
            weatherDto = modelMapper.map(weathers.get(i), WeatherCreateDTO.class);
            weatherDto.setCityId(cityModel.getId());
            weatherService.saveWeather(weatherDto);
        }
    }
}
