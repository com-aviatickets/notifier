package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailBatchRequestValidator implements ConstraintValidator<ValidEmailBatchRequest, EmailBatchRequest> {

    @Override
    public boolean isValid(EmailBatchRequest request, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (request == null) {
            return false;
        }

        boolean isValid = true;

        if (request.getEmails() == null || request.getEmails().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Emails cannot be empty")
                    .addPropertyNode("emails")
                    .addConstraintViolation();
            isValid = false;
        } else {
            int index = 0;
            for (String email : request.getEmails()) {
                if (email == null || !ValidationUtils.EMAIL_PATTERN.matcher(email).matches()) {
                    context.buildConstraintViolationWithTemplate("Invalid email format")
                            .addPropertyNode("emails")
                            .inIterable().atIndex(index)
                            .addConstraintViolation();
                    isValid = false;
                }
                index++;
            }
        }

        isValid &= ValidationUtils.validateSubject(request.getSubject(), context);
        isValid &= ValidationUtils.validateText(request.getText(), context);
        isValid &= ValidationUtils.validateSendAt(request.getSendAt(), context);

        return isValid;
    }
}
