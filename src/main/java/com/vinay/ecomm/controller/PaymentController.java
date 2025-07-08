package com.vinay.ecomm.controller;

import com.razorpay.RazorpayException;
import com.vinay.ecomm.dto.PaymentOrderRequest;
import com.vinay.ecomm.entity.PaymentOrder;
import com.vinay.ecomm.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin("*")
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<Map<String,Object>> createOrder(@RequestBody PaymentOrder order){
        try {
            String razorpayResponse = paymentService.createOrder(order);
            JSONObject json = new JSONObject(razorpayResponse);

            // Create a proper JSON response for frontend
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", json.getString("id")); // Razorpay order ID
            response.put("amt", order.getAmt());    // Total amount
            response.put("currency", "INR");

            return ResponseEntity.ok(response); // Send valid JSON
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating order"));
        }
    }

    @PostMapping("/update-order")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Map<String, String> data){
        String paymentId = data.get("paymentId");
        String orderId = data.get("orderId");
        String status = data.get("status");

        paymentService.updateOrderStatus(paymentId, orderId, status);
        return ResponseEntity.ok("Order updated Successfully and Email sent");
    }

}
