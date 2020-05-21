package com.allanweber.api.user;

import com.allanweber.api.configuration.ApplicationConfiguration;
import com.allanweber.api.registration.UserRegistration;
import com.allanweber.api.registration.events.UserRegistrationEvent;
import com.allanweber.api.user.mapper.UserMapper;
import com.allanweber.api.user.repository.UserDetailsHelper;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final String USER_AUTH_NAME = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final ApplicationConfiguration applicationConfiguration;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserEntity user = userRepository.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            throw new UsernameNotFoundException("No authorities for the user");
        }
        return UserDetailsHelper.createUserDetails(user);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Boolean userNameExists(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public Boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto createUser(UserRegistration userRegistration) {

        if (userNameExists(userRegistration.getUserName())) {
            throw new HttpClientErrorException(BAD_REQUEST, String.format("User name %s already exists", userRegistration.getUserName()));
        }

        if (emailExists(userRegistration.getEmail())) {
            throw new HttpClientErrorException(BAD_REQUEST, String.format("Email %s already exists", userRegistration.getEmail()));
        }

        UserEntity userToSave = mapper.mapToEntity(userRegistration);

        userToSave.setPassword(encoder.encode(userToSave.getPassword()));
        userToSave.setEnabled(!applicationConfiguration.isEmailVerificationEnabled());
        userToSave.setVerified(!applicationConfiguration.isEmailVerificationEnabled());
        userToSave.addAuthority(USER_AUTH_NAME);
        UserEntity userSaved = userRepository.save(userToSave);
        UserDto userDto = mapper.mapToDto(userSaved);
        if (applicationConfiguration.isEmailVerificationEnabled()) {
            eventPublisher.publishEvent(new UserRegistrationEvent(userDto));
        }
        return userDto;
    }

    public void setUserVerified(String userName) {
        UserEntity user = userRepository.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        user.setEnabled(true);
        user.setVerified(true);
        userRepository.save(user);
    }

    public void enableTwoFactorAuthentication(String userName) {
        changeTwoFactorAuthentication(userName, true);
    }

    public void disableTwoFactorAuthentication(String userName) {
        changeTwoFactorAuthentication(userName, false);
    }

    private void changeTwoFactorAuthentication(String userName, boolean value) {
        UserEntity user = userRepository.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        user.setTwoFactor(value);
        userRepository.save(user);
    }

    public UserDto get(String userName) {
        return userRepository.findById(userName)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND, "User not found"));
    }
}
