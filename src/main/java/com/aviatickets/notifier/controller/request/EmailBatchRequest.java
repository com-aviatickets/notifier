package com.aviatickets.notifier.controller.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class EmailBatchRequest {

    private List<String> emails;
    private String subject;
    private String text;
    private LocalDateTime sendAt;

    private static final Logger logger = LoggerFactory.getLogger(EmailBatchRequest.class);

    public EmailBatchRequest(List<String> emails, String subject, String text, LocalDateTime sendAt) {
        this.emails = emails;
        this.subject = subject;
        this.text = text;
        this.sendAt = sendAt;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusFive = now.plusMinutes(5);

        logger.info("Validating batch email: current time={}, sendAt={}", now, sendAt);

        if (emails == null || emails.isEmpty()) {
            logger.error("Batch email validation failed: empty email list");
            return false;
        }

        if (!isValidSubject()) {
            logger.error("Batch email validation failed: subject length invalid ({})", subject != null ? subject.length() : 0);
            return false;
        }

        if (!isValidText()) {
            logger.error("Batch email validation failed: text is empty");
            return false;
        }

        if (sendAt != null && sendAt.isBefore(nowPlusFive)) {
            logger.error("Batch email validation failed: sendAt {} is too early (must be after {})", sendAt, nowPlusFive);
            return false;
        }

        if (!areEmailsValid()) {
            logger.error("Batch email validation failed: invalid email format in list");
            return false;
        }

        return true;
    }

    private boolean isValidSubject() {
        return subject != null && subject.length() >= 10 && subject.length() <= 100;
    }

    private boolean isValidText() {
        return text != null && !text.isEmpty();
    }

    private boolean areEmailsValid() {
        return emails.stream().allMatch(email -> email.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    @Override
    public String toString() {
        return "BatchEmailRequest{" +
                "emails=" + emails +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", sendAt=" + sendAt +
                '}';
    }
}
