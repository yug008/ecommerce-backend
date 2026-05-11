package com.example.Ecommerce_Application.repository;

import com.example.Ecommerce_Application.entity.OrderItem;
import com.example.Ecommerce_Application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByProduct(Product product);

}
