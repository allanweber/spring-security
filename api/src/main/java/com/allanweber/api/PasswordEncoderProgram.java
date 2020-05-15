package com.allanweber.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderProgram {
    public static void main(String[] args) {
        String encoded=new BCryptPasswordEncoder().encode("user");
        System.out.println(encoded);
    }
}
