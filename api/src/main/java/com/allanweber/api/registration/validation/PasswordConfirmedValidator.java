package com.allanweber.api.registration.validation;

import com.allanweber.api.registration.UserRegistration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmedValidator implements ConstraintValidator<PasswordConfirmed, UserRegistration> {

    @Override
    public boolean isValid(UserRegistration user, ConstraintValidatorContext context) {
        String password = user.getPassword();
        String confirmedPassword = user.getConfirmPassword();
        return password.equals(confirmedPassword);
    }

}