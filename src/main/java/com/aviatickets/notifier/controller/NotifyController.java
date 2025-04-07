package com.aviatickets.notifier.controller;


import com.aviatickets.notifier.controller.request.EmailBatchRequest;
import com.aviatickets.notifier.controller.request.EmailRequest;
import com.aviatickets.notifier.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/internal/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final EmailService emailService;

    @PostMapping("")
    public ResponseEntity<Void> notify(@RequestBody @Valid EmailRequest request) {
        emailService.saveEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> notifyBatch(@RequestBody @Valid EmailBatchRequest request) {
        emailService.saveBatchEmails(request);
        return ResponseEntity.ok().build();
    }



}
