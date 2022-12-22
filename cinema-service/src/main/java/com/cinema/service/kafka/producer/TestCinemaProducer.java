package com.cinema.service.kafka.producer;

import com.cinema.service.kafka.dto.CancelNotificationRequest;
import com.cinema.service.kafka.dto.NotificationRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestCinemaProducer implements CinemaProducer {

    @Override
    public void sendNotificationRequest(NotificationRequest request) {

    }

    @Override
    public void sendCancelNotificationRequest(CancelNotificationRequest request) {

    }
}
