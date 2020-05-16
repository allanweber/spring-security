package com.allanweber.api.registration.events;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailVerificationListenerTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void send() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("New account created");
        message.setText("Account activation link: http://localhost:8080/verify/email?id=" + UUID.randomUUID().toString());
        message.setTo("a.cassianoweber@gmail.com");
        mailSender.send(message);
    }
}