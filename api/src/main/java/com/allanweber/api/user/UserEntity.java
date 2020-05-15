package com.allanweber.api.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

    public static User create(String username, String firstname, String lastname, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(password);
        user.setEmail(email);
        user.setEnabled(true);
        return user;
    }
}
