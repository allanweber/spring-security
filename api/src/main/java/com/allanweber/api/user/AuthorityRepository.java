package com.allanweber.api.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorityRepository   extends CrudRepository<AuthorityEntity, Integer> {
    List<AuthorityEntity> findByUsername(String userName);
}
