package com.aviatickets.notifier.service;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import com.aviatickets.notifier.controller.request.EmailRequest;
import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import com.aviatickets.notifier.repository.NotifierEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final NotifierEmailRepository notifierEmailRepository;

    public void saveEmail(EmailRequest request) {
        Email email = Email.builder()
                .externalId(UUID.randomUUID())
                .email(request.getEmail())
                .subject(request.getSubject())
                .text(request.getText())
                .requiredSendingDatetime(request.getSendAt())
                .sendingDatetime(null)
                .status(determineStatus(request.getSendAt()))
                .build();

        notifierEmailRepository.save(email);
    }

    public void saveBatchEmails(EmailBatchRequest request) {
        List<Email> emails = request.getEmails().stream()
                .map(email -> Email.builder()
                        .externalId(UUID.randomUUID())
                        .email(email)
                        .subject(request.getSubject())
                        .text(request.getText())
                        .requiredSendingDatetime(request.getSendAt())
                        .sendingDatetime(null)
                        .status(determineStatus(request.getSendAt()))
                        .build())
                .toList();

        notifierEmailRepository.saveAll(emails);
    }

    private EmailStatus determineStatus(LocalDateTime sendAt) {
        return (sendAt == null || sendAt.isBefore(LocalDateTime.now())) ? EmailStatus.SENDED : EmailStatus.DELAYED;
    }
}
