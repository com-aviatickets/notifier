package com.aviatickets.notifier.job;

import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import com.aviatickets.notifier.repository.NotifierEmailRepository;
import com.aviatickets.notifier.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class EmailSchedulerTest {

    @Mock
    private NotifierEmailRepository notifierEmailRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailScheduler emailScheduler;

    @Captor
    private ArgumentCaptor<Email> emailCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessDelayedEmails_successfulSend() {

        Email delayedEmail = Email.builder()
                .externalId(UUID.randomUUID())
                .email("delayed@example.com")
                .subject("Subject")
                .text("Body")
                .status(EmailStatus.DELAYED)
                .requiredSendingDatetime(LocalDateTime.now().minusMinutes(10))
                .build();

        when(notifierEmailRepository.findAllByStatusAndRequiredSendingDatetimeBefore(
                eq(EmailStatus.DELAYED), any(LocalDateTime.class)))
                .thenReturn(List.of(delayedEmail));


        emailScheduler.processDelayedEmails();


        verify(emailService, times(1)).sendEmailNow(delayedEmail);
        verify(notifierEmailRepository, times(1)).save(emailCaptor.capture());

        Email updatedEmail = emailCaptor.getValue();
        assert updatedEmail.getStatus() == EmailStatus.SENDED;
        assert updatedEmail.getSendingDatetime() != null;
    }

    @Test
    void testProcessDelayedEmails_withException() {
        Email problematicEmail = Email.builder()
                .externalId(UUID.randomUUID())
                .email("fail@example.com")
                .subject("Oops")
                .text("Broken")
                .status(EmailStatus.DELAYED)
                .requiredSendingDatetime(LocalDateTime.now().minusMinutes(15))
                .build();

        when(notifierEmailRepository.findAllByStatusAndRequiredSendingDatetimeBefore(
                eq(EmailStatus.DELAYED), any(LocalDateTime.class)))
                .thenReturn(List.of(problematicEmail));

        doThrow(new RuntimeException("Simulated failure"))
                .when(emailService).sendEmailNow(problematicEmail);


        emailScheduler.processDelayedEmails();


        verify(emailService, times(1)).sendEmailNow(problematicEmail);
        verify(notifierEmailRepository, never()).save(any());
    }
}
