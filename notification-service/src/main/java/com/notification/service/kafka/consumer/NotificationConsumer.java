package com.notification.service.kafka.consumer;

import com.notification.service.kafka.dto.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class NotificationConsumer {
//    @KafkaListener(topics = "${com.notification.service.kafka.request.topic}",
    @KafkaListener(topics = "notification-request", groupId = "notification")
//    public void consume(NotificationResponse response) {
        public void consume(String response) {
        log.info("Received: {}", response);
    }
}
