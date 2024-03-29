package com.vova.laba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vova.laba.model.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w WHERE w.city.id = :city AND w.temp >= :minTemp AND w.temp <= :maxTemp")
    Optional<List<Weather>> findWeatherByCityIdAndTemperature(@Param("city") Long cityId,
            @Param("minTemp") Float minTemp,
            @Param("maxTemp") Float maxTemp);
}
