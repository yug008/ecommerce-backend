package com.example.Ecommerce_Application.repository;

import com.example.Ecommerce_Application.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Ecommerce_Application.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);            //find cart by user
}
