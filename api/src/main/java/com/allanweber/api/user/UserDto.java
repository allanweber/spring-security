package com.allanweber.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private Integer id;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private Boolean enabled;
}
