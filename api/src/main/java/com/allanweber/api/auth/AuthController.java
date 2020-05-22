package com.allanweber.api.auth;

import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping(path = "/login", produces = "application/json")
    public ResponseEntity<UserDto> login(Authentication authentication) {
        return ok(userService.get(((User)authentication.getPrincipal()).getUsername()));
    }

    @GetMapping(path = "/authenticated", produces = "application/json")
    public ResponseEntity<AuthDto> authenticated(Authentication authentication) {
        AuthDto dto = new AuthDto(Optional.ofNullable(authentication)
                .map(Authentication::isAuthenticated)
                .orElse(false));
        if(dto.isAuthenticated()){
            assert authentication != null;
            dto.setUser(userService.get(((User)authentication.getPrincipal()).getUsername()));
        }
        return ok(dto);
    }
}
