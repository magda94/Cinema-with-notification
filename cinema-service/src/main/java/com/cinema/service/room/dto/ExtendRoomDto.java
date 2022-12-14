package com.cinema.service.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendRoomDto {

    private int roomId;

//    private Set<SeatDto> seats;
//
//    private int totalReservedNumber;

    private Set<ShowInfo> showInfo;
}
