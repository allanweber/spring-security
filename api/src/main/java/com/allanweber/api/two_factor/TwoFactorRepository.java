package com.allanweber.api.two_factor;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TwoFactorRepository extends CrudRepository<TwoFactor, String> {
    Optional<TwoFactor> findByUsername(String username);
    boolean existsByUsername(String username);
    Long deleteByUsername(String username);
}
