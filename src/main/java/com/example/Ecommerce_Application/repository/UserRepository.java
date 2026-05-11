package com.example.Ecommerce_Application.repository;

import com.example.Ecommerce_Application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);                  //used during login to find the user
    Boolean existsByEmail(String email);                       //used during register to check if email already taken

}
