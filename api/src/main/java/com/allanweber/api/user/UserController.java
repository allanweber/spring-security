package com.allanweber.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ok(userService.getAll());
    }

    @PutMapping("/{userId}/enable-two-factor")
    public ResponseEntity<?> enableTwoFactorAuthentication(@PathVariable("userId") Integer userId){
        userService.enableTwoFactorAuthentication(userId);
        return ok().build();
    }

    @PutMapping("/{userId}/disable-two-factor")
    public ResponseEntity<?> disableTwoFactorAuthentication(@PathVariable("userId") Integer userId){
        userService.disableTwoFactorAuthentication(userId);
        return ok().build();
    }
}
