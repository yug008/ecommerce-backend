package com.example.Ecommerce_Application.repository;

import com.example.Ecommerce_Application.entity.Cart;
import com.example.Ecommerce_Application.entity.CartItem;
import com.example.Ecommerce_Application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByProduct(Product product);
}
