package com.aviatickets.notifier.service;

import com.aviatickets.notifier.dto.UserData;
import com.aviatickets.notifier.dto.UserEvent;
import com.aviatickets.notifier.model.NotifierUser;
import com.aviatickets.notifier.repository.NotifierUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserSyncService {

    private static final Logger log = LoggerFactory.getLogger(UserSyncService.class);

    private final NotifierUserRepository notifierUserRepository;

    public UserSyncService(NotifierUserRepository notifierUserRepository) {
        this.notifierUserRepository = notifierUserRepository;
    }

    public void handleUserEvent(UserEvent event) {
        if (event.getOperation() == null) {
            log.error("Operation is null for user event");
            throw new IllegalArgumentException("Unknown operation: null");
        }

        UserData data = event.getData();

        switch (event.getOperation()) {
            case CREATE -> {
                NotifierUser user = new NotifierUser();
                user.setId(data.getId());
                user.setEmail(data.getEmail());

                notifierUserRepository.save(user);
                log.info("Created user with id {}", data.getId());
            }

            case UPDATE -> {
                notifierUserRepository.findById(data.getId()).ifPresentOrElse(existingUser -> {
                    existingUser.setEmail(data.getEmail());
                    notifierUserRepository.save(existingUser);
                    log.info("Updated user with id {}", data.getId());
                }, () -> {
                    log.error("Cannot update user — user with id {} not found", data.getId());
                });
            }

            default -> {
                log.error("Unknown operation type: {}", event.getOperation());
                throw new IllegalArgumentException("Unknown operation: " + event.getOperation());
            }
        }
    }
}
