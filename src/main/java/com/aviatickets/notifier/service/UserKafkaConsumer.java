package com.aviatickets.notifier.service;

import com.aviatickets.notifier.dto.UserEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer {

    private final UserSyncService userSyncService;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "profileUserSync", groupId = "notifier-group")
    public void listen(ConsumerRecord<String, String> record) {
        UserEvent event;
        try {
            event = objectMapper.readValue(record.value(), UserEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userSyncService.handleUserEvent(event);
    }
}
