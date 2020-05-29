package com.allanweber.api.auth;

import com.allanweber.api.configuration.AuthoritiesHelper;
import com.allanweber.api.jwt.JwtUtils;
import com.allanweber.api.jwt.TokenDto;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public LoginResponse login(@Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUser(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = (User) authentication.getPrincipal();

        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var tokenDto = jwtUtils.generateJwtToken(user.getUsername(), roles);

        AuthUser authUser = getAuthUser(user.getUsername());

        return new LoginResponse("bearer", tokenDto.getToken(), roles, tokenDto.getExpirationIn(), tokenDto.getIssuedAt(), authUser);
    }

    public LoginResponse loginSocial(String authHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userName = authentication.getPrincipal().toString();
        var user = userService.loadUserByUsername(userName);
        var roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        AuthUser authUser = getAuthUser(user.getUsername());
        TokenDto tokenDto = jwtUtils.openToken(authHeader);
        return new LoginResponse("bearer", tokenDto.getToken(), roles, tokenDto.getExpirationIn(), tokenDto.getIssuedAt(), authUser);
    }

    public TokenDto refreshToken(@NotBlank String authHeader) {
        return jwtUtils.refreshToken(authHeader);
    }

    public Boolean isAdmin(String authHeader) {
        return jwtUtils.getRoles(authHeader).contains(AuthoritiesHelper.ROLE_ADMIN);
    }

    public AuthUser getAuthUser() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return getAuthUser(user);
    }

    private AuthUser getAuthUser(String userName) {
        var userDto = userService.get(userName);
        return new AuthUser(userName, Optional.ofNullable(userDto.getFirstName()).orElse(userDto.getUserName()));
    }
}
