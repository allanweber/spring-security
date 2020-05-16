package com.allanweber.api.registration;

import com.allanweber.api.registration.events.UserRegistrationEvent;
import com.allanweber.api.registration.verification.VerificationService;
import com.allanweber.api.user.User;
import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
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

    public UserDto register(UserRegistration user) {
        String password = encoder.encode(user.getPassword());
        User userEntity = User.create(user.getUserName(),user.getFirstName(),user.getLastName(), password, user.getEmail());
        UserDto userDto = userService.createUser(userEntity);

        eventPublisher.publishEvent(new UserRegistrationEvent(userDto));

        return userDto;
    }

    public void verify(String id) {
        String userName = verificationService.getUserNameForId(id);
        userService.serUserVerified(userName);
        verificationService.remove(id);
    }
}
