package com.vova.laba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vova.laba.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
