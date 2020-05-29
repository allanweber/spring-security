package com.allanweber.api.registration;

import com.allanweber.api.registration.validation.PasswordConfirmed;
import com.allanweber.api.registration.validation.PasswordPolicy;
import com.allanweber.api.registration.validation.UniqueEmail;
import com.allanweber.api.registration.validation.UniqueUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
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

    private boolean oauth;

    public UserRegistration(@NotEmpty(message = "Enter your First Name") String firstName, @NotEmpty(message = "Enter your Last Name") String lastName, @NotEmpty(message = "Enter your User Name") String userName, @NotEmpty(message = "Enter your Email") @Email(message = "Email is not valid") String email, @NotEmpty(message = "Enter your Password") String password, @NotEmpty(message = "Confirm your Password") String confirmPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserRegistration(@NotEmpty(message = "Enter your First Name") String firstName, @NotEmpty(message = "Enter your Last Name") String lastName, @NotEmpty(message = "Enter your User Name") String userName, @NotEmpty(message = "Enter your Email") @Email(message = "Email is not valid") String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.oauth = true;
    }
}
