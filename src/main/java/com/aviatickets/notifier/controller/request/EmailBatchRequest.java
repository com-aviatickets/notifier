package com.aviatickets.notifier.controller.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.aviatickets.notifier.validation.ValidEmailBatchRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
@Getter
@AllArgsConstructor
@ValidEmailBatchRequest
public class EmailBatchRequest {

    private UUID id;
    private List<String> emails;
    private String subject;
    private String text;
    private LocalDateTime sendAt;

    @Override
    public String toString() {
        return "EmailBatchRequest{" +
                "emails=" + emails +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", sendAt=" + sendAt +
                '}';
    }
}
