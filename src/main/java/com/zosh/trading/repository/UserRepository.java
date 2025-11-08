package com.zosh.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.trading.model.User;


public interface UserRepository extends JpaRepository <User, Long> {

    // Add this method: Spring Data JPA will look for a 'User' with a matching 'email' field.
    User findByEmail(String email);



}
