package com.example.Ecommerce_Application.controller;

import com.example.Ecommerce_Application.dto.response.OrderResponse;
import com.example.Ecommerce_Application.service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Value("${razorpay.key.id}")
    private String keyId;

    // ── Step 1: Create Razorpay Order ────────────────────────────────
    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createRazorpayOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails) throws RazorpayException {

        String razorpayOrderId = paymentService.createRazorpayOrder(orderId);

        // Return razorpayOrderId + keyId to frontend
        // Frontend needs both to open Razorpay checkout
        return ResponseEntity.ok(Map.of(                                //we have used 'Map.of()' here instead of ResponseDTO because we are only returning 2 fields - razorpayOrderId and keyId to the user.
                "razorpayOrderId", razorpayOrderId,
                "keyId", keyId
        ));
    }

    // ── Step 2: Verify Payment ───────────────────────────────────────
    @PostMapping("/verify")
    public ResponseEntity<OrderResponse> verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) throws RazorpayException {

        OrderResponse response = paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );
        return ResponseEntity.ok(response);
    }

    // ── Get Key ID (for frontend) ────────────────────────────────────
    @GetMapping("/key")
    public ResponseEntity<?> getKey() {
        return ResponseEntity.ok(Map.of("keyId", keyId));
    }
}