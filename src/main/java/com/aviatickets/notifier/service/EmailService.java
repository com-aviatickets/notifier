package com.aviatickets.notifier.service;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import com.aviatickets.notifier.controller.request.EmailRequest;
import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import com.aviatickets.notifier.repository.NotifierEmailRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final NotifierEmailRepository notifierEmailRepository;

    public void saveEmailToDatabase(EmailRequest request) {
        if (request.isValid()) {
            Email email = Email.builder()
                    .externalId(UUID.randomUUID())
                    .email(request.getEmail())
                    .subject(request.getSubject())
                    .text(request.getText())
                    .requiredSendingDatetime(request.getSendAt())
                    .sendingDatetime(null)
                    .status(request.getSendAt() == null || request.getSendAt().isBefore(LocalDateTime.now()) ? EmailStatus.SENDED : EmailStatus.DELAYED)
                    .build();
            notifierEmailRepository.save(email);
        } else {
            throw new BadRequestException("Invalid email request");
        }
    }


    public void saveBatchEmailsToDatabase(EmailBatchRequest request) {
        if (request.isValid()) {
            List<Email> emails = request.getEmails().stream().map(email -> {
                return Email.builder()
                        .externalId(UUID.randomUUID())
                        .email(email)
                        .subject(request.getSubject())
                        .text(request.getText())
                        .requiredSendingDatetime(request.getSendAt())
                        .sendingDatetime(null)
                        .status(request.getSendAt() == null || request.getSendAt().isBefore(LocalDateTime.now()) ? EmailStatus.SENDED : EmailStatus.DELAYED)
                        .build();
            }).toList();

            notifierEmailRepository.saveAll(emails);
        } else {
            throw new BadRequestException("Invalid batch email request");
        }
    }
}

