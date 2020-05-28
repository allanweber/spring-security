package com.allanweber.api.user.repository.listener;

import com.allanweber.api.configuration.AuthoritiesHelper;
import com.allanweber.api.user.repository.Authority;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ApiStartListener implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        insertUserAdmin();
        insertUser();
    }

    private void insertUserAdmin() {
        if(userRepository.findById("admin").isPresent()){
            return;
        }
        List<Authority> authorities = Arrays.asList(new Authority(AuthoritiesHelper.ADMIN), new Authority(AuthoritiesHelper.USER));
        UserEntity admin = new UserEntity(
                "admin",
                "$2a$10$hbxecwitQQ.dDT4JOFzQAulNySFwEpaFLw38jda6Td.Y/cOiRzDFu",
                "a.cassianoweber@gmail.com",
                true,
                authorities,
                true
        );
        userRepository.insert(admin);
    }

    private void insertUser() {
        if(userRepository.findById("user").isPresent()){
            return;
        }
        List<Authority> authorities = Collections.singletonList(new Authority(AuthoritiesHelper.USER));
        UserEntity user = new UserEntity(
                "user",
                "$2a$10$EUxPWS43H55VHJS6OtNdwOVtRwNprMGnwq0sXPhdwVpJo9v5oKYjO",
                "a.cassianoweber@gmail.com",
                true,
                authorities,
                true
        );
        userRepository.insert(user);
    }
}
