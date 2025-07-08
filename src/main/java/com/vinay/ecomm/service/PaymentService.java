package com.vinay.ecomm.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.vinay.ecomm.entity.PaymentOrder;
import com.vinay.ecomm.repoistory.OrderRepo;
import com.vinay.ecomm.repoistory.PaymentRepo;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String secretKey;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;


    public String createOrder(PaymentOrder orderDetails) throws RazorpayException {

        RazorpayClient client = new RazorpayClient(keyId,secretKey);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",(int)(orderDetails.getAmt()*100));
        orderRequest.put("currency","INR");
        orderRequest.put("receipt","txn_"+ UUID.randomUUID());

        Order razorpayOrder = client.orders.create(orderRequest);
        System.out.println(razorpayOrder.toString());
        orderDetails.setOrderId(razorpayOrder.get("id"));
        orderDetails.setStatus("created");
        orderDetails.setCreatedAt(LocalDateTime.now());

        paymentRepo.save(orderDetails);
        System.out.println("Razorpay Order Request: " + orderRequest.toString());
        System.out.println("Razorpay Order Response: " + razorpayOrder.toString());
        return razorpayOrder.toString();


    }

    public void updateOrderStatus(String paymentId, String orderId, String status) {

        PaymentOrder order = paymentRepo.findByOrderId(orderId);
        order.setPaymentId(paymentId);
        order.setStatus(status);
        paymentRepo.save(order);

        if("Success".equalsIgnoreCase(status)){
            // 1. Send email
            emailService.sendEmail(order.getEmail(), order.getName(), order.getAmt());

            // 2. Restore productQuantities (you need to store it earlier or pass it now)
            // Example (this part depends on your frontend — maybe store in PaymentOrder or use cache/session):

            // 3. Save actual order & order items
            orderService.saveOrderAfterPayment(order, null);
        }
    }
}
