package com.allanweber.api.two_factor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    @GetMapping("/setup-two-factor")
    public ResponseEntity<?> getTwoFactorQrCode(Principal principal) {
        String url = twoFactorService.getTwoFactorQrCode(principal.getName());
        return ok(new QrCodeDto(url));
    }
}
