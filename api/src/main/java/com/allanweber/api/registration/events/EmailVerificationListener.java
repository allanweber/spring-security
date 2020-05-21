package com.allanweber.api.registration.events;

import com.allanweber.api.configuration.ApplicationConfiguration;
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
    private final ApplicationConfiguration applicationConfiguration;

    @Override
    public void onApplicationEvent(UserRegistrationEvent userRegistrationEvent) {
        String verificationId = verificationService.createVerification(userRegistrationEvent.getUser().getUserName());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("New account created");
        String text = String.format("Account activation link: %s?id=%s", applicationConfiguration.getVerificationUrl(), verificationId);
        message.setText(text);
        message.setTo(userRegistrationEvent.getUser().getEmail());
        mailSender.send(message);
    }
}
