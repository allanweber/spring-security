package com.allanweber.api.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, String> {
    User findByUsername(String userName);

    Boolean existsByUsername(String userName);

    Boolean existsByEmail(String email);
}
