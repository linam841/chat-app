package com.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    @KafkaListener(topics = "messages.in-review", groupId = "moderation-service-group")
    public void listen(String message) {
        log.info("Received message: {}", message);
    }

}