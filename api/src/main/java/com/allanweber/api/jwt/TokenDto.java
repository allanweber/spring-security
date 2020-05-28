package com.allanweber.api.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TokenDto {
    private String token;
    private long expirationIn;
    private Date issuedAt;
}
