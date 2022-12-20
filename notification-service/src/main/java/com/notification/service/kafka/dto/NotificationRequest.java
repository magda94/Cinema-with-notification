package com.notification.service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private Instant showTime;

    private String user;

    private String filmName;

    private Place place;
}
