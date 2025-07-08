package com.vinay.ecomm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,String name,double amt){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Payment Successful- Vishal Mart Thanks for shopping! ");
        message.setText("Total Order Amount: "+amt+" your order will be delivered soon...");

        mailSender.send(message);
    }
}
