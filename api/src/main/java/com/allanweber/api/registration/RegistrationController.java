package com.allanweber.api.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRegistrationService registrationService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserRegistration user) {
        return ok(registrationService.register(user));
    }
}
