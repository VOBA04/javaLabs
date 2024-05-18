package com.vova.laba.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "city")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "cityName", unique = true)
  private String cityName;

  @OneToMany(
      mappedBy = "city",
      cascade = {CascadeType.REMOVE, CascadeType.MERGE})
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<Weather> weather;

  @ManyToMany
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @JoinTable(
      name = "city_user",
      joinColumns = @JoinColumn(name = "city_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> users = new HashSet<>();

  public void addUser(User user) {
    users.add(user);
    user.getCities().add(this);
  }

  public void deleteUser(Long userId) {
    User user = users.stream().filter(t -> t.getId().equals(userId)).findFirst().orElse(null);
    if (user != null) {
      users.remove(user);
      user.getCities().remove(this);
    }
  }
}
