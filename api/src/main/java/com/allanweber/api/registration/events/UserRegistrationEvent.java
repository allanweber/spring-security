package com.allanweber.api.registration.events;

import com.allanweber.api.user.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {

    private final UserDto user;

    public UserRegistrationEvent(UserDto user) {
        super(user);
        this.user = user;
    }
}
