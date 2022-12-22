package com.cinema.service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelNotificationRequest {
    private Instant showTime;

    private String user;

    private String filmName;
}
