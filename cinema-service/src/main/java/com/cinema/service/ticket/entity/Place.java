package com.cinema.service.ticket.entity;

import com.cinema.service.ticket.dto.PlaceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private int roomId;

    private int rowNumber;

    private int columnNumber;

    public PlaceDto toPlaceDto() {
        return PlaceDto.builder()
                .roomId(this.roomId)
                .rowNumber(this.rowNumber)
                .columnNumber(this.columnNumber)
                .build();
    }
}
