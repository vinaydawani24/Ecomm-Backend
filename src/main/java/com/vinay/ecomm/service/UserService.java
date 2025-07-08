package com.vinay.ecomm.service;

import com.vinay.ecomm.entity.User;
import com.vinay.ecomm.repoistory.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        try
        {
            User newUser = userRepo.save(user);
            System.out.println("User Added to database");
            return newUser;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public User loginUser(String email, String password) {
        //check if user is there or not
        User user = userRepo.findByEmail(email);
        if(user!=null && user.getPassword().equals(password))
        {
            return user;
        }
        return null;// invalid credentials
    }


    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
