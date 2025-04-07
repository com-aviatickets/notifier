package com.aviatickets.notifier.controller.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.aviatickets.notifier.validation.ValidEmailRequest;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@ValidEmailRequest
public class EmailRequest {
    private UUID id;
    private String email;
    private String subject;
    private String text;
    private LocalDateTime sendAt;
}
