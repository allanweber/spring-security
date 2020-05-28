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

    private Boolean enabled = false;

    private Boolean verified = false;

    private List<Authority> authorities;

    public UserEntity(String username, String password, String email, Boolean enabled, List<Authority> authorities, Boolean verified) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.authorities = authorities;
        this.verified = verified;
    }

    public void addAuthority(String authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(new Authority(authority));
    }
}
