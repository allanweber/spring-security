package com.allanweber.api.registration.verification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_verification")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Verification {

    @Id
    private String id;

    private String username;

    public Verification(String username) {
        this.username = username;
    }
}