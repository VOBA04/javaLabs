package com.vova.laba.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  private String name;

  @ManyToMany(mappedBy = "users", cascade = CascadeType.MERGE)
  @EqualsAndHashCode.Exclude
  private Set<City> cities = new HashSet<>();

  public void addCity(City city) {
    cities.add(city);
    city.getUsers().add(this);
  }

  public void deleteCity(Long cityId) {
    City city = cities.stream().filter(t -> t.getId().equals(cityId)).findFirst().orElse(null);
    if (city != null) {
      cities.remove(city);
      city.getUsers().remove(this);
    }
  }
}
