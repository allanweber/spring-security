package com.allanweber.api.registration.verification;

import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserService userService;

    public String createVerification(String username) {
        if (!verificationRepository.existsByUsername(username)) {
            Verification verification = new Verification(username);
            verificationRepository.save(verification);
        }
        return getVerificationIdByUsername(username);
    }

    public void verify(String id) {
        String userName = getUserNameForId(id);
        userService.setUserVerified(userName);
        remove(id);
    }

    private String getVerificationIdByUsername(String username) {
        String id = null;
        Verification verification = verificationRepository.findByUsername(username);
        if (verification != null) {
            id = verification.getId();
        }
        return id;
    }

    private String getUserNameForId(String id) {
        Optional<Verification> verification = verificationRepository.findById(id);
        return verification.map(Verification::getUsername)
                .orElseThrow(() -> new HttpClientErrorException(BAD_REQUEST, "Invalid verification id"));
    }

    private void remove(String id) {
        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(BAD_REQUEST, "Invalid verification id"));
        verificationRepository.delete(verification);
    }
}
