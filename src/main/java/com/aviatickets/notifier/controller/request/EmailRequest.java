package com.aviatickets.notifier.controller.request;

import java.time.LocalDateTime;
import java.util.UUID;

public class EmailRequest {

    private UUID id;
    private String email;
    private String subject;
    private String text;
    private LocalDateTime sendAt;

    public EmailRequest(String email, String subject, String text, LocalDateTime sendAt) {
        this.email = email;
        this.subject = subject;
        this.text = text;
        this.sendAt = sendAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public boolean isValid() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$") &&
                subject != null && subject.length() >= 10 && subject.length() <= 100 &&
                text != null && !text.isEmpty() &&
                (sendAt == null || sendAt.isAfter(LocalDateTime.now().plusMinutes(5)));
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", sendAt=" + sendAt +
                '}';
    }
}
