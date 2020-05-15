package com.allanweber.api.registration;

import com.allanweber.api.registration.validation.PasswordConfirmed;
import com.allanweber.api.registration.validation.PasswordPolicy;
import com.allanweber.api.registration.validation.UniqueEmail;
import com.allanweber.api.registration.validation.UniqueUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@PasswordConfirmed
public class UserRegistration {

    @NotEmpty(message = "Enter your First Name")
    private String firstName;

    @NotEmpty(message = "Enter your Last Name")
    private String lastName;

    @NotEmpty(message = "Enter your User Name")
    @UniqueUsername
    private String userName;

    @NotEmpty(message = "Enter your Email")
    @Email(message = "Email is not valid")
    @UniqueEmail
    private String email;

    @NotEmpty(message = "Enter your Password")
    @PasswordPolicy
    private String password;

    @NotEmpty(message = "Confirm your Password")
    private String confirmPassword;
}
