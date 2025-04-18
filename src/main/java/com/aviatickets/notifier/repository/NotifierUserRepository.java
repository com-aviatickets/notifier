package com.aviatickets.notifier.repository;

import com.aviatickets.notifier.model.NotifierUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifierUserRepository extends JpaRepository<NotifierUser, Long> {
}
