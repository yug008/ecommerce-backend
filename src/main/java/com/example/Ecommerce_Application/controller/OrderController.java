package com.example.Ecommerce_Application.controller;

import com.example.Ecommerce_Application.dto.request.OrderRequest;
import com.example.Ecommerce_Application.dto.response.OrderResponse;
import com.example.Ecommerce_Application.enums.OrderStatus;
import com.example.Ecommerce_Application.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(service.placeOrder(request, userDetails.getUsername()));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(service.getMyOrders(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById (@PathVariable Long id){
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        return ResponseEntity.ok(service.getAllOrders());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status){
        return ResponseEntity.ok(service.updateOrderStatus(id,status));
    }

}
