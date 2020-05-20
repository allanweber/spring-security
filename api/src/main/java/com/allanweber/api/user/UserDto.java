package com.allanweber.api.user;

import com.allanweber.api.user.repository.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean enabled;

    private List<Authority> authorities;
}
