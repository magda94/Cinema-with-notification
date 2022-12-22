package com.cinema.service.kafka.producer;

import com.cinema.service.kafka.dto.CancelNotificationRequest;
import com.cinema.service.kafka.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        ListenableFuture<SendResult<String, Object>> future  = kafkaTemplate.send(topic, request);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message: " + request
                        + " with offset: " + result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message : " + request, ex);
            }
        });
    }

    public void sendCancelNotificationRequest(CancelNotificationRequest request) {
        ListenableFuture<SendResult<String, Object>> future  = kafkaTemplate.send(cancelTopic, request);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message: " + request
                        + " with offset: " + result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message : " + request, ex);
            }
        });
    }
}
