package com.allanweber.api.two_factor;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class TwoFactor {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String username;

    private String secret;

    public TwoFactor(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    public TwoFactor() {
    }
}
