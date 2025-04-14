package com.aviatickets.notifier.service;

import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import com.aviatickets.notifier.controller.request.EmailRequest;
import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import com.aviatickets.notifier.repository.NotifierEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final NotifierEmailRepository notifierEmailRepository;

    private final JavaMailSender mailSender;


    public void saveEmail(EmailRequest request) {

        UUID externalId = (request.getId() != null) ? UUID.fromString(String.valueOf(request.getId())) : UUID.randomUUID();

        Email email = Email.builder()
                .externalId(externalId)
                .email(request.getEmail())
                .subject(request.getSubject())
                .text(request.getText())
                .requiredSendingDatetime(request.getSendAt())
                .sendingDatetime(null)
                .status(determineStatus(request.getSendAt()))
                .build();

        if (email.getStatus() == EmailStatus.SENDED) {
            sendEmailNow(email);
            email.setSendingDatetime(LocalDateTime.now());
        }

        notifierEmailRepository.save(email);
    }



    public void saveBatchEmails(EmailBatchRequest request) {
        List<Email> emails = request.getEmails().stream()
                .map(emailAddress -> {
                    Email email = Email.builder()
                            .externalId(UUID.randomUUID())
                            .email(emailAddress)
                            .subject(request.getSubject())
                            .text(request.getText())
                            .requiredSendingDatetime(request.getSendAt())
                            .sendingDatetime(null)
                            .status(determineStatus(request.getSendAt()))
                            .build();

                    if (email.getStatus() == EmailStatus.SENDED) {
                        sendEmailNow(email);
                        email.setSendingDatetime(LocalDateTime.now());
                    }

                    return email;
                })
                .toList();

        notifierEmailRepository.saveAll(emails);
    }


    public void sendEmailNow(Email email) {
        if (email.getEmail() == null || email.getSubject() == null || email.getText() == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getEmail());
        message.setSubject(email.getSubject());
        message.setText(email.getText());

        mailSender.send(message);
    }


    private EmailStatus determineStatus(LocalDateTime sendAt) {
        return (sendAt == null || sendAt.isBefore(LocalDateTime.now())) ? EmailStatus.SENDED : EmailStatus.DELAYED;
    }
}
