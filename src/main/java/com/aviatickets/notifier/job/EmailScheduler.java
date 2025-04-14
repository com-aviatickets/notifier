package com.aviatickets.notifier.job;

import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import com.aviatickets.notifier.repository.NotifierEmailRepository;
import com.aviatickets.notifier.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final NotifierEmailRepository notifierEmailRepository;
    private final EmailService emailService;


    private static final Logger logger = LoggerFactory.getLogger(EmailScheduler.class);

    @Scheduled(fixedRate = 300000)
    public void processDelayedEmails() {
        List<Email> delayedEmails = notifierEmailRepository
                .findAllByStatusAndRequiredSendingDatetimeBefore(EmailStatus.DELAYED, LocalDateTime.now());

        for (Email email : delayedEmails) {
            try {
                emailService.sendEmailNow(email);
                email.setStatus(EmailStatus.SENDED);
                email.setSendingDatetime(LocalDateTime.now());
                notifierEmailRepository.save(email);
            } catch (Exception e) {

                logger.error("Ошибка при отправке письма с ID: {}. Ошибка: {}", email.getExternalId(), e.getMessage(), e);
            }
        }
    }
}
