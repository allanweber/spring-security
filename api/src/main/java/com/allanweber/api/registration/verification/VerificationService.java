package com.allanweber.api.registration.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    public String createVerification(String username) {
        String id;
        if (!verificationRepository.existsByUsername(username)) {
            Verification verification = new Verification(username);
            verification = verificationRepository.save(verification);
            id = verification.getId();
        }
        id = getVerificationIdByUsername(username);
        return id;
    }

    public String getVerificationIdByUsername(String username) {
        String id = null;
        Verification verification = verificationRepository.findByUsername(username);
        if (verification != null) {
            id = verification.getId();
        }
        return id;
    }

    public String getUserNameForId(String id) {
        Optional<Verification> verification = verificationRepository.findById(id);
        return verification.map(Verification::getUsername)
                .orElseThrow(() -> new HttpClientErrorException(BAD_REQUEST, "Invalid verification id"));
    }

    public void remove(String id) {
        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(BAD_REQUEST, "Invalid verification id"));
        verificationRepository.delete(verification);
    }
}
