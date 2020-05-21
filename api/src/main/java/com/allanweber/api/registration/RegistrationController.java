package com.allanweber.api.registration;

import com.allanweber.api.registration.verification.VerificationService;
import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final VerificationService verificationService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserRegistration user) {
        return ok(userService.createUser(user));
    }

    @GetMapping("/verify/email")
    public ResponseEntity<?> verify(@RequestParam("id") String id) {
        verificationService.verify(id);
        return ok().build();
    }
}
