package com.cinema.service.kafka.producer;

import com.cinema.service.kafka.dto.CancelNotificationRequest;
import com.cinema.service.kafka.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DevCinemaProducer implements CinemaProducer {

    @Value("${com.cinema.service.kafka.request.reservation.topic}")
    private String topic;

    @Value("${com.cinema.service.kafka.request.cancel.topic}")
    private String cancelTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotificationRequest(NotificationRequest request) {
        kafkaTemplate.send(topic, request);
        log.info("Sent message: '{}'", request.toString());
    }

    public void sendCancelNotificationRequest(CancelNotificationRequest request) {
        kafkaTemplate.send(cancelTopic, request);
        log.info("Sent message: '{}'", request.toString());
    }
}
