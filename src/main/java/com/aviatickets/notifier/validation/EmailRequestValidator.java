package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EmailRequestValidator implements ConstraintValidator<ValidEmailRequest, EmailRequest> {

    @Override
    public boolean isValid(EmailRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            buildViolation(context, "Request cannot be null");
            return false;
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if (request.getEmail() == null || !ValidationUtils.EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            buildViolation(context, "Invalid email format");
            isValid = false;
        }

        if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
            buildViolation(context, "Subject cannot be empty");
            isValid = false;
        }

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            buildViolation(context, "Text cannot be empty");
            isValid = false;
        }

        if (request.getSendAt() != null && request.getSendAt().isBefore(LocalDateTime.now().minusMinutes(1))) {
            buildViolation(context, "SendAt cannot be in the past");
            isValid = false;
        }

        return isValid;
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
