package com.vova.laba.repository;

import com.vova.laba.model.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
  Optional<City> findCityByCityName(String cityName);

  @Query("SELECT c FROM City as c ORDER BY c.cityName ASC")
  List<City> findAllSorted();
}
