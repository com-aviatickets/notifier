package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EmailBatchRequestValidatorTest {

    private EmailBatchRequestValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EmailBatchRequestValidator();
        context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void testValidRequest() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("user@example.com"));
        request.setSubject("Valid Subject Title");
        request.setText("This is a valid email body text.");
        request.setSendAt(LocalDateTime.now().plusMinutes(5));

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void testEmptySubject() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("user@example.com"));
        request.setSubject("");
        request.setText("Some text");
        request.setSendAt(LocalDateTime.now().plusMinutes(5));

        assertFalse(validator.isValid(request, context));
    }

    // Остальные тесты — аналогично
}
