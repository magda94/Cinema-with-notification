package com.notification.service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    private int roomId;

    private int rowNumber;

    private int columnNumber;
}
