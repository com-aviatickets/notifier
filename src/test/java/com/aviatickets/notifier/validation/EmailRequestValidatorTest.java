package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailRequestValidatorTest {

    private EmailRequestValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EmailRequestValidator();
        context = mock(ConstraintValidatorContext.class);


        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));
    }

    @Test
    void testValidRequest() {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setSubject("Test Subject");
        request.setText("Test body of the email");
        request.setSendAt(LocalDateTime.now().plusHours(1));

        boolean result = validator.isValid(request, context);
        assertTrue(result);
    }

    @Test
    void testNullRequest() {
        boolean result = validator.isValid(null, context);
        assertFalse(result);
    }

    @Test
    void testInvalidEmail() {
        EmailRequest request = new EmailRequest();
        request.setEmail("invalid-email");
        request.setSubject("Subject");
        request.setText("Text");
        request.setSendAt(LocalDateTime.now());

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }

    @Test
    void testEmptySubject() {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setSubject("");
        request.setText("Text");
        request.setSendAt(LocalDateTime.now());

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }

    @Test
    void testEmptyText() {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setSubject("Subject");
        request.setText("");
        request.setSendAt(LocalDateTime.now());

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }

    @Test
    void testTooOldSendAt() {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setSubject("Subject");
        request.setText("Text");
        request.setSendAt(LocalDateTime.now().minusYears(1)); // слишком старая дата

        boolean result = validator.isValid(request, context);
        assertFalse(result);
    }
}
