package com.allanweber.api.user.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean enabled;

    private List<Authority> authorities;

    public UserEntity(String username, String password, String email, Boolean enabled, List<Authority> authorities) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public void addAuthority(String authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(new Authority(authority));
    }
}
