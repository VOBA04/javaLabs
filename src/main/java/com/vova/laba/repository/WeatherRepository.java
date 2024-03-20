package com.vova.laba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vova.laba.model.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
