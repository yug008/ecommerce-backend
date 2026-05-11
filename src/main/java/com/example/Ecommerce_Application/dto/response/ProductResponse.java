package com.example.Ecommerce_Application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private Double price;
    private String category;
    private String imageUrl;
    private LocalDateTime createdAt;
}
