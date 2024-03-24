package com.vova.laba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vova.laba.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findCityByCityName(String cityName);

    @Query("SELECT c FROM City c JOIN c.weather w WHERE c.id = :cityId AND w.temp >= :minTemp AND w.temp <= :maxTemp")
    Optional<City> findCityWeatherByTemperature(@Param("cityId") Long cityId, @Param("minTemp") Float minTemp,
            @Param("maxTemp") Float maxTemp);
}
