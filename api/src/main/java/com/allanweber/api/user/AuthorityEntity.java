package com.allanweber.api.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

    public static Authority create(String username, String authority) {
        Authority auth = new Authority();
        auth.setUsername(username);
        auth.setAuthority(authority);
        return auth;
    }
}
