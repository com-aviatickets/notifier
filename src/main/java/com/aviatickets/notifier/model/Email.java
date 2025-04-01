package com.aviatickets.notifier.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifier_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifier_email_seq")
    @SequenceGenerator(
            name = "notifier_email_seq",
            sequenceName = "notifier_email_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID externalId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String text;

    @Column(nullable = true)
    private LocalDateTime requiredSendingDatetime;

    @Column(nullable = true)
    private LocalDateTime sendingDatetime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status;
}
