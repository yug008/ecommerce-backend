package com.example.Ecommerce_Application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank(message = "Address is required.")
    private String address;
}
