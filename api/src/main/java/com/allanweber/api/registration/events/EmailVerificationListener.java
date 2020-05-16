package com.allanweber.api.registration.events;

import com.allanweber.api.registration.verification.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationListener implements ApplicationListener<UserRegistrationEvent> {

    private final JavaMailSender mailSender;
    private final VerificationService verificationService;

    @Override
    public void onApplicationEvent(UserRegistrationEvent userRegistrationEvent) {
        String verificationId = verificationService.createVerification(userRegistrationEvent.getUser().getUsername());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("New account created");
        message.setText("Account activation link: http://localhost:8080/registration/verify/email?id=" + verificationId);
        message.setTo(userRegistrationEvent.getUser().getEmail());
        mailSender.send(message);
    }
}
