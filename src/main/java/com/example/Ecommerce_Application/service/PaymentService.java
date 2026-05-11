package com.example.Ecommerce_Application.service;

import com.example.Ecommerce_Application.dto.response.OrderResponse;
import com.example.Ecommerce_Application.entity.Order;
import com.example.Ecommerce_Application.enums.OrderStatus;
import com.example.Ecommerce_Application.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Value("${razorpay.currency}")
    private String currency;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // ── Step 1: Create Razorpay Order ────────────────────────────────
    public String createRazorpayOrder(Long orderId) throws RazorpayException {

        // Fetch our order from DB
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        // Create Razorpay order request
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(order.getTotalAmount() * 100)); // paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", "order_" + orderId);

        // Call Razorpay API
        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        // Save razorpayOrderId in our order
        order.setRazorpayOrderId(razorpayOrder.get("id"));
        orderRepository.save(order);

        // Return razorpayOrderId to frontend
        return razorpayOrder.get("id");
    }

    // ── Step 2: Verify Payment ───────────────────────────────────────
    public OrderResponse verifyPayment(String razorpayOrderId,
                                       String razorpayPaymentId,
                                       String razorpaySignature) throws RazorpayException {

        // Verify signature
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", razorpayOrderId);
        attributes.put("razorpay_payment_id", razorpayPaymentId);
        attributes.put("razorpay_signature", razorpaySignature);

        try {
            com.razorpay.Utils.verifyPaymentSignature(attributes, keySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Payment verification failed! " + e.getMessage());
        }

        Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        // Update order status to PAID
        order.setRazorpayPaymentId(razorpayPaymentId);
        order.setRazorpaySignature(razorpaySignature);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return orderService.getOrderById(order.getId());
    }
}