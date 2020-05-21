package com.allanweber.api.two_factor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TwoFactorRepository extends MongoRepository<TwoFactor, String> {
}
