package com.vova.laba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vova.laba.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findCityByCityName(String cityName);
}
