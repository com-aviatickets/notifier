package com.aviatickets.notifier.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailBatchRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailBatchRequest {
    String message() default "Invalid batch email request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
