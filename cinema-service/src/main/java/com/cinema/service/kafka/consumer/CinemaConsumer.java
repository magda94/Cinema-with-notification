package com.cinema.service.kafka.consumer;

import com.cinema.service.kafka.dto.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CinemaConsumer {

    @KafkaListener(topics = "${com.cinema.service.kafka.response.topic}",
    groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, NotificationResponse> consumerRecord) {
        log.info("Received: {}", consumerRecord.value());
    }
}
