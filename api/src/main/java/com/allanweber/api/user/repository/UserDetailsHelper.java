package com.allanweber.api.user.repository;

import com.allanweber.api.two_factor.AuthoritiesHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetailsHelper {

    public static UserDetails createUserDetails(UserEntity user) {
        List<String> authorities = new ArrayList<>();
        if (user.getTwoFactor()) {
            authorities.add(AuthoritiesHelper.TWO_AUTH_AUTHORITY);
        } else {
            authorities.add(AuthoritiesHelper.ROLE_USER);
            if( user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))){
                authorities.add("ROLE_ADMIN");
            }
        }
        return User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .disabled(!user.getVerified())
                .authorities(buildAuthorities(authorities))
                .build();
    }

    private static List<GrantedAuthority> buildAuthorities(List<String> authorities) {
        List<GrantedAuthority> authList = new ArrayList<>(1);
        for (String authority : authorities) {
            authList.add(new SimpleGrantedAuthority(authority));
        }
        return authList;
    }
}
