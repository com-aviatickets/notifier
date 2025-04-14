package com.aviatickets.notifier.validation;

import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    public static boolean validateSubject(String subject, ConstraintValidatorContext context) {
        if (subject == null || subject.trim().isEmpty()) {
            addViolation(context, "subject", "Subject cannot be empty");
            return false;
        }

        if (subject.length() < 10 || subject.length() > 100) {
            addViolation(context, "subject", "Subject must be between 10 and 100 characters");
            return false;
        }

        return true;
    }

    public static boolean validateText(String text, ConstraintValidatorContext context) {
        if (text == null || text.trim().isEmpty()) {
            addViolation(context, "text", "Text cannot be empty");
            return false;
        }

        if (text.length() < 10 || text.length() > 500) {
            addViolation(context, "text", "Text must be between 10 and 500 characters");
            return false;
        }

        return true;
    }

    public static boolean validateSendAt(LocalDateTime sendAt, ConstraintValidatorContext context) {
        if (sendAt != null && sendAt.isBefore(LocalDateTime.now().minusMinutes(1))) {
            addViolation(context, "sendAt", "SendAt cannot be in the past");
            return false;
        }
        return true;
    }

    public static void addViolation(ConstraintValidatorContext context, String field, String message) {
        context.disableDefaultConstraintViolation();
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                context.buildConstraintViolationWithTemplate(message);

        if (field != null) {
            builder.addPropertyNode(field).addConstraintViolation();
        } else {
            builder.addConstraintViolation();
        }
    }
}
