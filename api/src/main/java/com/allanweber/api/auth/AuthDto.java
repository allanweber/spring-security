package com.allanweber.api.auth;

import com.allanweber.api.user.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AuthDto {

    private boolean authenticated;

    private UserDto user;

    public AuthDto(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
