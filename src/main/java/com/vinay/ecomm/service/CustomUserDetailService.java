package com.vinay.ecomm.service;

import com.vinay.ecomm.dto.CustomUserDetail;
import com.vinay.ecomm.entity.User;
import com.vinay.ecomm.repoistory.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user =  userRepo.findByUsername(username);
               if (user == null) {
        throw new UsernameNotFoundException("User with username " + username + " not found.");
    }
        System.out.println("Login attempt for username: " + username);
        return new CustomUserDetail(user);
    }
}
