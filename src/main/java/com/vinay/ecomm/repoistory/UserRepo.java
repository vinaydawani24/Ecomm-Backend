package com.vinay.ecomm.repoistory;

import com.vinay.ecomm.entity.Orders;
import com.vinay.ecomm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByUsername(String username);
}
