package com.cinema.service.room.entity;

import com.cinema.service.room.dto.RoomDto;
import com.cinema.service.show.entity.ShowEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"seats", "shows"})
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "room_id", unique = true)
    private int roomId;

    @Column(name = "total_rows_number", nullable = false)
    private int rowsNumber;

    @Column(name = "total_columns_number", nullable = false)
    private int columnsNumber;

    @OneToMany(mappedBy = "room") //field in SeatEntity
    private Set<SeatEntity> seats;

    @OneToMany(mappedBy = "room") //field in ShowEntity
    private Set<ShowEntity> shows;

    public RoomDto toRoomDto() {
        return RoomDto.builder()
                .roomId(this.roomId)
                .columnsNumber(this.columnsNumber)
                .rowsNumber(this.rowsNumber)
                .build();
    }
}
