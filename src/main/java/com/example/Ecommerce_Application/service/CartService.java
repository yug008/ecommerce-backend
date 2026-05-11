package com.example.Ecommerce_Application.service;

import com.example.Ecommerce_Application.dto.response.CartItemResponse;
import com.example.Ecommerce_Application.dto.response.CartResponse;
import com.example.Ecommerce_Application.entity.Cart;
import com.example.Ecommerce_Application.entity.CartItem;
import com.example.Ecommerce_Application.entity.Product;
import com.example.Ecommerce_Application.entity.User;
import com.example.Ecommerce_Application.repository.CartItemRepository;
import com.example.Ecommerce_Application.repository.CartRepository;
import com.example.Ecommerce_Application.repository.ProductRepository;
import com.example.Ecommerce_Application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    public CartResponse addToCart(Long productId, Integer quantity, String email) {
        //Find existing cart or create new cart of the user
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .user(user)
                    .build();
            return cartRepository.save(newCart);
        });

        //  Find product
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found!"));

        // Check if product already in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(newItem);
        }

        //  Reload cart and return
        Cart updatedCart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found!"));
        return mapToResponse(updatedCart);
    }

    public CartResponse getCart(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found."));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found."));
        return mapToResponse(cart);
    }

    public CartResponse removeFromCart(Long id, String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found."));
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()->new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
        Cart newCart = cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found."));
        return mapToResponse(newCart);
    }

    public void clearCart(String email){                      //we will clear the cart items inside the cart of the user , not delete the cart of the user
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found."));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(item -> CartItemResponse.builder()
                        .cartItemId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .imageUrl(item.getProduct().getImageUrl())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}


/*
We first find user using its email by using repo methods. Then find the user's respective cart.
 */