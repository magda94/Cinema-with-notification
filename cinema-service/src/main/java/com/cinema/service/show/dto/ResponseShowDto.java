package com.cinema.service.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseShowDto {

    private int showId;

    private int filmId;

    private int roomId;

    private Instant startDate;

    private Instant endDate;
}
