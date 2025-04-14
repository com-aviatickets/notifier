package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailBatchRequestValidatorTest {

    private EmailBatchRequestValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EmailBatchRequestValidator();
        context = mock(ConstraintValidatorContext.class);


        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));
    }

    @Test
    void testValidRequest() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("test@example.com", "user@domain.com"));
        request.setSubject("Hello");
        request.setText("This is a test message");
        request.setSendAt(LocalDateTime.now().plusMinutes(5));

        boolean result = validator.isValid(request, context);
        assertTrue(result);
    }

    @Test
    void testInvalidEmails() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("invalid-email", null));
        request.setSubject("Valid Subject");
        request.setText("Valid text");
        request.setSendAt(LocalDateTime.now().plusMinutes(1));

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }

    @Test
    void testEmptyEmailList() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of());
        request.setSubject("Subject");
        request.setText("Text");
        request.setSendAt(LocalDateTime.now());

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }

    @Test
    void testNullRequest() {
        boolean result = validator.isValid(null, context);
        assertFalse(result);
    }

    @Test
    void testInvalidSubjectAndText() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("test@example.com"));
        request.setSubject("");
        request.setText("");
        request.setSendAt(LocalDateTime.now());

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }

    @Test
    void testInvalidSendAt() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("test@example.com"));
        request.setSubject("Subject");
        request.setText("Text");
        request.setSendAt(LocalDateTime.now().minusYears(1));

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }
}
