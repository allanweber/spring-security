package com.allanweber.api.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
@Data
public class User {

    @Id
    private String username;

    private String password;

    private String email;

    private Boolean enabled;
}
