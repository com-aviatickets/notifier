package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailRequestValidator implements ConstraintValidator<ValidEmailRequest, EmailRequest> {

    @Override
    public boolean isValid(EmailRequest request, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (request == null) {
            return false;
        }

        boolean valid = true;

        valid &= ValidationUtils.validateEmail(request.getEmail(), context);
        valid &= ValidationUtils.validateSubject(request.getSubject(), context);
        valid &= ValidationUtils.validateText(request.getText(), context);
        valid &= ValidationUtils.validateSendAt(request.getSendAt(), context);

        return valid;
    }
}
