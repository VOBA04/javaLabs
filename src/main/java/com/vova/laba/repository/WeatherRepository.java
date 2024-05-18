package com.vova.laba.repository;

import com.vova.laba.model.Weather;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
  @Query(
      "SELECT w FROM Weather w WHERE w.city.id = :city"
          + " AND w.temp >= :minTemp AND w.temp <= :maxTemp")
  Optional<List<Weather>> findWeatherByCityIdAndTemperature(
      @Param("city") Long cityId, @Param("minTemp") Float minTemp, @Param("maxTemp") Float maxTemp);

  @Query(
      "SELECT w FROM Weather as w ORDER BY w.city.cityName ASC, w.year ASC, w.month ASC, w.day"
          + " ASC")
  Optional<List<Weather>> findAllSorted();

  @Query(
      "SELECT w FROM Weather w WHERE w.city.id = :city ORDER BY w.year ASC, w.month ASC, w.day ASC")
  Optional<List<Weather>> findWeatherByCityIdSorted(@Param("city") Long cityId);
}
