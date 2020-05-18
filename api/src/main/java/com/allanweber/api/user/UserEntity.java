package com.allanweber.api.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String firstname;

    private String lastname;

    private String password;

    private String email;

    private Boolean enabled;

    private Boolean verified;

    private Boolean twoFactor;

    public static UserEntity create(String username, String firstname, String lastname, String password, String email) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(password);
        user.setEmail(email);
        user.setEnabled(true);
        user.setVerified(false);
        user.setTwoFactor(false);
        return user;
    }

    public void markVerified() {
        this.setVerified(true);
    }

    public void enableTwoFactor() {
        this.setTwoFactor(true);
    }
}
