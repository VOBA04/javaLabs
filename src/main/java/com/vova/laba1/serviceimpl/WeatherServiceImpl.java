package com.vova.laba1.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vova.laba1.payload.CityCoordinatesResponse;
import com.vova.laba1.payload.OpenWeatherReport;
import com.vova.laba1.payload.WeatherResponse;
import com.vova.laba1.service.WeatherService;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Value("${weatherservice.key}")
    private String key;

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
    public WeatherResponse getWeather(CityCoordinatesResponse coord) {
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
            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setTemp(responseBody.getMain().getTemp());
            weatherResponse.setHumidity(responseBody.getMain().getHumidity());
            weatherResponse.setPressure(responseBody.getMain().getPressure());
            weatherResponse.setSpeed(responseBody.getWind().getSpeed());
            weatherResponse.setDeg(responseBody.getWind().getDeg());
            weatherResponse.setClouds(responseBody.getClouds().getAll());
            return weatherResponse;
        } else {
            return null;
        }
    }
}
