package com.notification.service.kafka.producer;

import com.notification.service.kafka.dto.NotificationResponse;
import com.notification.service.kafka.dto.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    @Value("${com.notification.service.kafka.response.topic}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotificationResponse(NotificationResponse notificationResponse) {
        kafkaTemplate.send(topic, notificationResponse);
        log.info("Sent message: '{}'", notificationResponse.toString());
    }
}
