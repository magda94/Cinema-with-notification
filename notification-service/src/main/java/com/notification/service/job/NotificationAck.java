package com.notification.service.job;

import com.notification.service.kafka.dto.NotificationResponse;
import com.notification.service.kafka.dto.ResponseStatus;
import com.notification.service.kafka.producer.NotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationAck {

    private final NotificationProducer notificationProducer;

    public void sendNotificationAck(ResponseStatus status) {
        var response = NotificationResponse.builder()
                .responseStatus(status)
                .build();

        notificationProducer.sendNotificationResponse(response);
    }
}

