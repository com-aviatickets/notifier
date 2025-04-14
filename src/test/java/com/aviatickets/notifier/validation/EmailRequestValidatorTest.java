package com.aviatickets.notifier.validation;

import com.aviatickets.notifier.controller.request.EmailRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void testValidRequest() {
        EmailRequest request = new EmailRequest();
        request.setEmail("user@example.com");
        request.setSubject("Valid subject");
        request.setText("This is the body of the email.");
        request.setSendAt(LocalDateTime.now().plusMinutes(10));

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void testNullRequest() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testInvalidEmail() {
        EmailRequest request = new EmailRequest();
        request.setEmail("invalid-email");
        request.setSubject("Valid subject");
        request.setText("This is the body.");
        request.setSendAt(LocalDateTime.now().plusMinutes(10));

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void testEmptySubject() {
        EmailRequest request = new EmailRequest();
        request.setEmail("user@example.com");
        request.setSubject("");
        request.setText("Text body.");
        request.setSendAt(LocalDateTime.now().plusMinutes(10));

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void testEmptyText() {
        EmailRequest request = new EmailRequest();
        request.setEmail("user@example.com");
        request.setSubject("Some subject");
        request.setText("   ");
        request.setSendAt(LocalDateTime.now().plusMinutes(10));

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void testPastSendAt() {
        EmailRequest request = new EmailRequest();
        request.setEmail("user@example.com");
        request.setSubject("Some subject");
        request.setText("Some text");
        request.setSendAt(LocalDateTime.now().minusHours(1));

        assertFalse(validator.isValid(request, context));
    }
}
