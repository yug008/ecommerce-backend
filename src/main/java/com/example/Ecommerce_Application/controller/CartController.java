package com.example.Ecommerce_Application.controller;

import com.example.Ecommerce_Application.dto.response.CartResponse;
import com.example.Ecommerce_Application.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class CartController {

    @Autowired
    CartService service;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestParam Long productId, @RequestParam Integer quantity, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(service.addToCart(productId,quantity,userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(service.getCart(userDetails.getUsername()));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestParam Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(service.removeFromCart(id, userDetails.getUsername()));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails){
        service.clearCart(userDetails.getUsername());
        return ResponseEntity.ok("Cart is cleared successfully.");
    }
}


/*
We can't pass Email directly in the URL as @RequestParam or @PathVariable as its not safe because Anyone could pass any email and access other user's cart. So we access the email from the JWT Token which is secure.
Thus , we used :
@AuthenticationPrincipal UserDetails userDetails - To get logged in user
userDetails.getUsername() - gives you the email (which we pass as parameter to call service methods)

Never trust client for identity — always get it from token .
 */