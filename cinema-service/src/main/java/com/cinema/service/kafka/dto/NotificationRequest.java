package com.cinema.service.kafka.dto;

import com.cinema.service.ticket.entity.Place;
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
