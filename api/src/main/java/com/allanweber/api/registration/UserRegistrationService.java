package com.allanweber.api.registration;

import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.UserEntity;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserService userService;

    private final PasswordEncoder encoder;

    public UserDto register(UserRegistration user) {
        String password = encoder.encode(user.getPassword());
        UserEntity userEntity = UserEntity.create(user.getUserName(),user.getFirstName(),user.getLastName(), password, user.getEmail());
        return userService.createUser(userEntity);
    }
}
