package com.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "public-0", groupId = "my-group-id")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}