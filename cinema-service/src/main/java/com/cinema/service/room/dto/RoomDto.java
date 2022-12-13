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
public class RoomDto {

    @NotNull
    private int roomId;

    @NotNull
    @Max(value = 10)
    @Min(value = 1)
    private int rowsNumber;

    @NotNull
    @Max(value = 10)
    @Min(value = 1)
    private int columnsNumber;
}
