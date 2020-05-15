package com.allanweber.api.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorityRepository   extends CrudRepository<Authority, Integer> {
    List<Authority> findByUsername(String userName);
}
