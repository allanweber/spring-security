package com.allanweber.api.registration.verification;

import org.springframework.data.repository.CrudRepository;

public interface VerificationRepository extends CrudRepository<Verification, String> {

    Verification findByUsername(String username);
    boolean existsByUsername(String username);
}
