package com.allanweber.api.contact.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contacts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contact {

    @Id
    private String id;

    private String name;

    private int age;

    private String email;

    private String phone;
}
