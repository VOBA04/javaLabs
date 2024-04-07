package com.vova.laba.repository;

import com.vova.laba.model.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
  Optional<City> findCityByCityName(String cityName);
}
