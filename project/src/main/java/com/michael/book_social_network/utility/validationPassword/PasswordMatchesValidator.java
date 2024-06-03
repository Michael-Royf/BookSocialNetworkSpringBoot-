package com.michael.book_social_network.utility.validationPassword;


import com.michael.book_social_network.payload.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof RegistrationRequest) {
            RegistrationRequest request = (RegistrationRequest) obj;
            return request.getPassword().equals(request.getConfirmationPassword());
        }
//        else if (obj instanceof ChangePasswordRequest) {
//            ChangePasswordRequest passwordChangeRequest = (ChangePasswordRequest) obj;
//            return passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmationPassword());
//        }
        else
            return false;
    }
}
