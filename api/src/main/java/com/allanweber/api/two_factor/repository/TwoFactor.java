package com.allanweber.api.two_factor.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "two_factor")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TwoFactor {

    @Id
    private String userName;

    private String secret;
}
