package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EmailBatchRequestValidator implements ConstraintValidator<ValidEmailBatchRequest, EmailBatchRequest> {

    @Override
    public boolean isValid(EmailBatchRequest request, ConstraintValidatorContext context) {
        if (request == null) return false;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        List<String> emails = request.getEmails();
        if (emails == null || emails.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Email list cannot be empty")
                    .addConstraintViolation();
            valid = false;
        } else {
            for (String email : emails) {
                if (email == null || !ValidationUtils.EMAIL_PATTERN.matcher(email).matches()) {
                    context.buildConstraintViolationWithTemplate("Invalid email format in list")
                            .addConstraintViolation();
                    valid = false;
                    break;
                }
            }
        }

        valid &= ValidationUtils.validateSubject(request.getSubject(), context);
        valid &= ValidationUtils.validateText(request.getText(), context);
        valid &= ValidationUtils.validateSendAt(request.getSendAt(), context);

        return valid;
    }
}