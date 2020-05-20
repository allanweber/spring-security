package com.allanweber.api.user.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetailsHelper {

    public static UserDetails createUserDetails(UserEntity user) {
        String[] roles = user.getAuthorities().stream().map(Authority::getName).toArray(String[]::new);
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(roles).build();
    }

}
