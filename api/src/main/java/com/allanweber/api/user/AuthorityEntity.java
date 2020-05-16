package com.allanweber.api.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "authorities")
@Data
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String authority;

    public static AuthorityEntity create(String username, String authority) {
        AuthorityEntity auth = new AuthorityEntity();
        auth.setUsername(username);
        auth.setAuthority(authority);
        return auth;
    }
}
