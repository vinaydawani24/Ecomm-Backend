package com.vinay.ecomm.controller;

import com.vinay.ecomm.dto.OrderDTO;
import com.vinay.ecomm.entity.OrderRequest;
import com.vinay.ecomm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
//@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder/{userId}")
    public OrderDTO placeOrder(@PathVariable Long userId, @RequestBody OrderRequest orderRequest){
        return orderService.placeOrder(userId,orderRequest.getProductQuantities(),orderRequest.getTotalAmount()
        ,orderRequest.getRazorpayOrderId(),orderRequest.getRazorpayPaymentId());
    }

    @GetMapping("/all-orders")
    public List<OrderDTO> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrderByUser(@PathVariable Long userId){
        return orderService.getOrderByUser(userId);
    }

}
