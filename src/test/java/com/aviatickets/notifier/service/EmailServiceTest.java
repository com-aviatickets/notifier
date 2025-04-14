package com.aviatickets.notifier.service;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import com.aviatickets.notifier.controller.request.EmailRequest;
import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import com.aviatickets.notifier.repository.NotifierEmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private NotifierEmailRepository notifierEmailRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<Email> emailCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmail_withImmediateSend() {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setSubject("Test Subject");
        request.setText("Hello!");
        request.setSendAt(LocalDateTime.now().minusMinutes(1)); // Immediate send

        emailService.saveEmail(request);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(notifierEmailRepository, times(1)).save(emailCaptor.capture());

        Email savedEmail = emailCaptor.getValue();
        assertEquals("test@example.com", savedEmail.getEmail());
        assertEquals(EmailStatus.SENDED, savedEmail.getStatus());
        assertNotNull(savedEmail.getSendingDatetime());
    }

    @Test
    void testSaveEmail_withDelayedSend() {
        EmailRequest request = new EmailRequest();
        request.setEmail("future@example.com");
        request.setSubject("Future");
        request.setText("Will be sent later");
        request.setSendAt(LocalDateTime.now().plusHours(1)); // Delayed send

        emailService.saveEmail(request);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
        verify(notifierEmailRepository, times(1)).save(emailCaptor.capture());

        Email savedEmail = emailCaptor.getValue();
        assertEquals(EmailStatus.DELAYED, savedEmail.getStatus());
        assertNull(savedEmail.getSendingDatetime());
    }

    @Test
    void testSaveBatchEmails_mixedDelays() {
        EmailBatchRequest request = new EmailBatchRequest();
        request.setEmails(List.of("one@example.com", "two@example.com"));
        request.setSubject("Batch Subject");
        request.setText("Batch Text");
        request.setSendAt(LocalDateTime.now().minusMinutes(1)); // Immediate send

        emailService.saveBatchEmails(request);

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
        verify(notifierEmailRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSendEmailNow_withInvalidData() {
        Email email = Email.builder()
                .email(null) // no recipient
                .subject("subject")
                .text("text")
                .build();

        emailService.sendEmailNow(email);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}
