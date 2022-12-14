package com.cinema.service.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {

    private int roomId;

    private int rowNumber;

    private int columnNumber;
}
