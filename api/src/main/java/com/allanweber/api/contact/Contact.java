package com.allanweber.api.contact;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Contact {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int age;
    private String email;
    private String phone;
}
