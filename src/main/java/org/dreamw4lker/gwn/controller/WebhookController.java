package org.dreamw4lker.gwn.controller;

import lombok.extern.slf4j.Slf4j;
import org.dreamw4lker.gwn.domain.WebhookBean;
import org.dreamw4lker.gwn.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private final List<String> ALLOWED_EVENT_TYPES = List.of("push");
    private final String GITEA_EVENT_TYPE_HEADER = "X-Gitea-Event-Type";

    @Autowired
    private final EmailService emailService;

    public WebhookController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(path = "/handle")
    public ResponseEntity<?> handleWebhook(@RequestHeader(GITEA_EVENT_TYPE_HEADER) String eventType,
                                   @RequestParam("r") String recipients,
                                   @RequestBody WebhookBean webhookBean) {
        if (!ALLOWED_EVENT_TYPES.contains(eventType)) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
                    .body(String.format("Unsupported event type: '%s'", eventType));
        }

        try {
            emailService.sendSingleMessage(webhookBean, recipients);
        } catch (MessagingException e) {
            log.error("Failed to send message");
            log.error(e.getMessage());
        }
        return ResponseEntity
                .status(HttpServletResponse.SC_OK)
                .body(String.format("OK. Received '%s' event type", eventType));
    }
}
