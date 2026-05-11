package com.example.Ecommerce_Application.service;

import com.example.Ecommerce_Application.dto.request.OrderRequest;
import com.example.Ecommerce_Application.dto.response.OrderItemResponse;
import com.example.Ecommerce_Application.dto.response.OrderResponse;
import com.example.Ecommerce_Application.entity.*;
import com.example.Ecommerce_Application.enums.OrderStatus;
import com.example.Ecommerce_Application.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    // Place Order
    @Transactional
    public OrderResponse placeOrder(OrderRequest request, String email) {

        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        //Get cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found."));

        // Check cart not empty
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        // Create Order
        Order order = Order.builder()
                .user(user)
                .address(request.getAddress())
                .status(OrderStatus.PENDING)
                .totalAmount(cart.getTotalPrice())
                .items(new ArrayList<>())
                .build();

        // Convert CartItems → OrderItems + Step 6 decrement stock
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Check stock available
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            // Decrement stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Create OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();

            order.getItems().add(orderItem);
        }

        //  Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // Return OrderResponse
        return mapToResponse(savedOrder);
    }

    // Get All Orders for User
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //  Get Order By ID
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found."));
        return mapToResponse(order);
    }

    // Get All Orders (Admin)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update Order Status (Admin)
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found."));
        order.setStatus(status);
        return mapToResponse(orderRepository.save(order));
    }

    // Entity → ResponseDTO
    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .orderItemId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .imageUrl(item.getProduct().getImageUrl())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .address(order.getAddress())
                .createdAt(order.getCreatedAt())
                .items(items)
                .razorpayOrderId(order.getRazorpayOrderId())
                .build();
    }
}