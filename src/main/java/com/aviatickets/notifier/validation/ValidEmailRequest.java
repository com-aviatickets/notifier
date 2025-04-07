package com.aviatickets.notifier.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailRequest {
    String message() default "Invalid email request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
