package com.example.Ecommerce_Application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String status;
    private Double totalAmount;
    private String address;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private String razorpayOrderId;
}
