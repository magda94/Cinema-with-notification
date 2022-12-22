package com.cinema.service.kafka.producer;

import com.cinema.service.kafka.dto.CancelNotificationRequest;
import com.cinema.service.kafka.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CinemaProducer {

    @Value("${com.cinema.service.kafka.request.topic}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotificationRequest(NotificationRequest request) {
        kafkaTemplate.send(topic, request);
        log.info("Sent message: '{}'", request.toString());
    }

    public void sendCancelNotificationRequest(CancelNotificationRequest request) {
        kafkaTemplate.send(topic, request);
        log.info("Sent message: '{}'", request.toString());
    }
}
