package com.aviatickets.notifier.repository;

import com.aviatickets.notifier.model.Email;
import com.aviatickets.notifier.model.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotifierEmailRepository extends JpaRepository<Email, Long> {

    List<Email> findAllByStatusAndRequiredSendingDatetimeBefore(EmailStatus emailStatus, LocalDateTime now);
}
