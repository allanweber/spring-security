package com.allanweber.api.registration;

import com.allanweber.api.registration.events.UserRegistrationEvent;
import com.allanweber.api.registration.verification.VerificationService;
import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.UserEntity;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationService verificationService;

    @Value("${application.email-verification-enabled}")
    private Boolean emailVerificationEnabled;

    public UserDto register(UserRegistration user) {
        String password = encoder.encode(user.getPassword());
        UserEntity userEntity = UserEntity.create(user.getUserName(),user.getFirstName(),user.getLastName(), password, user.getEmail());
        UserDto userDto = userService.createUser(userEntity);

        if(emailVerificationEnabled) {
            eventPublisher.publishEvent(new UserRegistrationEvent(userDto));
        }
        else {
            userService.setUserVerified(userDto.getUsername());
        }

        return userDto;
    }

    public void verify(String id) {
        String userName = verificationService.getUserNameForId(id);
        userService.setUserVerified(userName);
        verificationService.remove(id);
    }
}
