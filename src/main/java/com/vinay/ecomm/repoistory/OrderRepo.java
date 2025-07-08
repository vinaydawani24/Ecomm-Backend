package com.vinay.ecomm.repoistory;

import com.vinay.ecomm.entity.Orders;
import com.vinay.ecomm.entity.User;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Orders,Long> {

    @Query("select o from Orders o join fetch o.user")
    List<Orders> findAllOrderWithUsers();
    List<Orders> findByUser(User user);



}
