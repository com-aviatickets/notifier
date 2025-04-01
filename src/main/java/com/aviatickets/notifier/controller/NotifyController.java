package com.aviatickets.notifier.controller;


import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import com.aviatickets.notifier.controller.request.EmailRequest;
import com.aviatickets.notifier.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/internal/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> notify(@RequestBody EmailRequest request) {
        emailService.saveEmailToDatabase(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> notifyBatch(@RequestBody EmailBatchRequest request) {
        emailService.saveBatchEmailsToDatabase(request);
        return ResponseEntity.ok().build();
    }



}
