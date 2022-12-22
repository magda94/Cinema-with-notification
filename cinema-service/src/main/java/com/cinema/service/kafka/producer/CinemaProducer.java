package com.cinema.service.kafka.producer;

import com.cinema.service.kafka.dto.CancelNotificationRequest;
import com.cinema.service.kafka.dto.NotificationRequest;

public interface CinemaProducer {

    public void sendNotificationRequest(NotificationRequest request);

    public void sendCancelNotificationRequest(CancelNotificationRequest request);
}
