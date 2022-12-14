package com.cinema.service.show.entity;

import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.dto.ShowDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "shows")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "show_id", nullable = false, unique = true)
    private int showId;

    @Column(name = "film_id", nullable = false)
    private int filmId;

    @Column(name = "start_data", nullable = false)
    private Instant startDate;

    @ManyToOne
    @JoinColumn(name = "room_id") //id in RoomEntity
    private RoomEntity room;

    public ShowDto toShowDto() {
        return ShowDto.builder()
                .showId(this.showId)
                .filmId(this.filmId)
                .roomId(this.room.getRoomId())
                .startDate(this.startDate)
                .build();
    }
}
