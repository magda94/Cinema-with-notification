package com.cinema.service.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {

    @NotNull
    private int roomId;

    @NotNull
    @Max(value = 10)
    @Min(value = 1)
    private int rowNumber;

    @NotNull
    @Max(value = 10)
    @Min(value = 1)
    private int columnNumber;

    private boolean reserved;
}
