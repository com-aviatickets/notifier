package com.aviatickets.notifier.validation;

import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

class ValidationUtils {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static boolean validateEmail(String email, ConstraintValidatorContext context) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            addViolation(context, "email", "Invalid email format");
            return false;
        }
        return true;
    }

    public static boolean validateSubject(String subject, ConstraintValidatorContext context) {
        if (subject == null || subject.length() < 10 || subject.length() > 100) {
            addViolation(context, "subject", "Subject must be between 10 and 100 characters");
            return false;
        }
        return true;
    }

    public static boolean validateText(String text, ConstraintValidatorContext context) {
        if (text == null || text.isBlank()) {
            addViolation(context, "text", "Text cannot be empty");
            return false;
        }
        return true;
    }

    public static boolean validateSendAt(LocalDateTime sendAt, ConstraintValidatorContext context) {
        if (sendAt != null && sendAt.isBefore(LocalDateTime.now().plusMinutes(5))) {
            addViolation(context, "sendAt", "Send time must be at least 5 minutes in the future");
            return false;
        }
        return true;
    }

    private static void addViolation(ConstraintValidatorContext context, String field, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
