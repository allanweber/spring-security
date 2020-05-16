package com.allanweber.api.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String userName);

    Boolean existsByUsername(String userName);

    Boolean existsByEmail(String email);
}
