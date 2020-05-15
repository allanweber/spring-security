package com.allanweber.api.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "authorities")
@Data
public class Authority {

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String authority;
}
