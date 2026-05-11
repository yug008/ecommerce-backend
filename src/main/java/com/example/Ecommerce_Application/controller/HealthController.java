//ITS PURPOSE IS ONLY FOR MAINTAINING THE UPTIME IN RENDER USING UPTIMEBOT
package com.example.Ecommerce_Application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "Ecommerce backend is running!"
        ));
    }
}