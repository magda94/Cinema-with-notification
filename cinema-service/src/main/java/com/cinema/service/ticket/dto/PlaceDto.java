package com.cinema.service.ticket.dto;

import com.cinema.service.ticket.entity.Place;
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

    public Place toPlace() {
        return Place.builder()
                .roomId(this.roomId)
                .rowNumber(this.rowNumber)
                .columnNumber(this.columnNumber)
                .build();
    }
}
