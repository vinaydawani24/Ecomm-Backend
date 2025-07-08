package com.vinay.ecomm.service;

import com.vinay.ecomm.dto.OrderDTO;
import com.vinay.ecomm.dto.OrderItemsDTO;
import com.vinay.ecomm.entity.*;
import com.vinay.ecomm.repoistory.OrderRepo;
import com.vinay.ecomm.repoistory.ProductRepo;
import com.vinay.ecomm.repoistory.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    public OrderDTO placeOrder(Long userId, Map<Long, Integer> productQuantities, double totalAmount,
                               String razorpayOrderId, String razorpayPaymentId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found."));
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setOrderDate(new Date());
        orders.setStatus("Pending");
        orders.setTotalAmt(totalAmount);

        List<OrderItems> orderItemsList = new ArrayList<>();
        List<OrderItemsDTO> orderItemsDTOS = new ArrayList<>();

        for(Map.Entry<Long,Integer> entry: productQuantities.entrySet()){
            Product product = productRepo.findById(entry.getKey())
                    .orElseThrow(()->new RuntimeException("Product not found."));

            OrderItems orderItems = new OrderItems();
            orderItems.setOrders(orders);
            orderItems.setProduct(product);
            orderItems.setQuantity(entry.getValue());
            orderItemsList.add(orderItems);

            orderItemsDTOS.add(new OrderItemsDTO(product.getName(),product.getPrice(),entry.getValue()));
        }

        orders.setRazorpayOrderId(razorpayOrderId);
        orders.setRazorpayPaymentId(razorpayPaymentId);
        orders.setStatus("Success");
        orders.setOrderItemsList(orderItemsList);
        Orders saveOrder = orderRepo.save(orders);

        return new OrderDTO(saveOrder.getId(),saveOrder.getTotalAmt(),saveOrder.getStatus(),
                saveOrder.getOrderDate(),orderItemsDTOS);

    }

    public List<OrderDTO> getAllOrders() {

        List<Orders> allOrderWithUsers = orderRepo.findAllOrderWithUsers();
        return allOrderWithUsers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Orders orders) {

        List<OrderItemsDTO> orderItems = orders.getOrderItemsList().stream()
                .map(items -> new OrderItemsDTO(
                        items.getProduct().getName(),
                        items.getProduct().getPrice(),
                        items.getQuantity())).collect(Collectors.toList());

        return new OrderDTO(
                orders.getId(),
                orders.getTotalAmt(),
                orders.getStatus(),
                orders.getOrderDate(),
                orders.getUser()!=null ? orders.getUser().getUsername() : "Unknown",
                orders.getUser() != null ? orders.getUser().getEmail() : "Unknown",
                orderItems
        );
    }

    public List<OrderDTO> getOrderByUser(Long userId) {
        Optional<User> userOp = userRepo.findById(userId);
        if(userOp.isEmpty())
        {
            throw  new RuntimeException("user not found");
        }
        User user= userOp.get();
        List<Orders> ordersList = orderRepo.findByUser(user);
        return ordersList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void saveOrderAfterPayment(PaymentOrder paymentOrder, Map<Long, Integer> productQuantities) {
        User user = userRepo.findByEmail(paymentOrder.getEmail());
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + paymentOrder.getEmail()));
        if (user == null) {
            throw new RuntimeException("User not found with email: " + paymentOrder.getEmail());
        }

        Orders orders = new Orders();
        orders.setUser(user);
        orders.setOrderDate(new Date());
        orders.setStatus("Paid");
        orders.setTotalAmt(paymentOrder.getAmt());

        orderRepo.save(orders); // saves both order and orderItems due to CascadeType.ALL
    }

}


