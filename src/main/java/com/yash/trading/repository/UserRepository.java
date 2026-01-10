package com.yash.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yash.trading.model.User;

// interface connecting to database
public interface UserRepository extends JpaRepository <User, Long> {

    // Add this method: Spring Data JPA will look for a 'User' with a matching 'email' field.
    User findByEmail(String email);



}
