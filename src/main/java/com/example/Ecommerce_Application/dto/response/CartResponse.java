package com.example.Ecommerce_Application.dto.response;

import com.example.Ecommerce_Application.entity.Cart;
import com.example.Ecommerce_Application.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> items;
    private double totalPrice;
}
