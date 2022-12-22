package com.notification.service.kafka.consumer;

import com.notification.service.kafka.dto.CancelNotificationRequest;
import com.notification.service.kafka.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "${com.notification.service.kafka.request.reservation.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
    containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, NotificationRequest> consumerRecord) {
        log.info("Received: {}", consumerRecord.value());
    }

    @KafkaListener(topics = "${com.notification.service.kafka.request.cancel.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
    containerFactory = "kafkaCancelListenerContainerFactory")
    public void consumeCancel(ConsumerRecord<String, CancelNotificationRequest> consumerRecord) {
        log.info("Received: {}", consumerRecord.value());
    }
}
