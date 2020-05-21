package com.allanweber.api.two_factor;

import com.allanweber.api.two_factor.repository.TwoFactor;
import com.allanweber.api.two_factor.repository.TwoFactorRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TwoFactorService {

    private final GoogleAuthenticator googleAuth = new GoogleAuthenticator();
    private final TwoFactorRepository twoFactorRepository;
    private static final String ISSUER = "AnyApp.com";

    public String getTwoFactorQrCode(String username) {
        GoogleAuthenticatorKey authKey = googleAuth.createCredentials();
        String secret = authKey.getKey();
        twoFactorRepository.deleteById(username);
        twoFactorRepository.save(new TwoFactor(username, secret));
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(ISSUER, username, authKey);
    }

    public boolean verifyCode(String username, int code) {
        TwoFactor twoFactor = twoFactorRepository.findById(username)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND, "Verification code not found for user"));
        return googleAuth.authorize(twoFactor.getSecret(), code);
    }
}
