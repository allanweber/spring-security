package com.allanweber.api.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository  extends MongoRepository<UserEntity, String> {
    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);
}