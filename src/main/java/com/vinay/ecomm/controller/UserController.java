package com.vinay.ecomm.controller;

import com.vinay.ecomm.entity.User;
import com.vinay.ecomm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
//@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }

//    @PostMapping("/login")
//    public User loginUser(@RequestBody User user)
//    {
//        return userService.loginUser(user.getEmail(),user.getPassword());
//    }

    @GetMapping("/all-user")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
