package com.allanweber.api.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactDto {

    private String id;

    private String name;

    private int age;

    private String email;

    private String phone;
}
