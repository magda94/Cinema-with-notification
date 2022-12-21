package com.cinema.service.kafka.producer;

import com.cinema.service.kafka.dto.NotificationRequest;
import com.cinema.service.ticket.entity.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

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

    @Scheduled(fixedRate = 10_000L)
    public void sentSchedule() {
        log.info("Starting sending message ...");
        var req = NotificationRequest.builder()
                .filmName("NAME")
                .user("USER")
                .showTime(Instant.now())
                .place(Place.builder()
                        .roomId(1)
                        .rowNumber(2)
                        .columnNumber(3)
                        .build())
                .build();

        sendNotificationRequest(req);
    }
}
