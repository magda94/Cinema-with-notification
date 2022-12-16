package com.cinema.service.show.entity;

import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.dto.RequestShowDto;
import com.cinema.service.show.dto.ResponseShowDto;
import com.cinema.service.ticket.entity.TicketEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "shows")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "tickets")
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

    @Column(name = "end_data", nullable = false)
    private Instant endDate;

    @ManyToOne
    @JoinColumn(name = "room_id") //id in RoomEntity
    private RoomEntity room;

    @OneToMany(mappedBy = "show")
    private Set<TicketEntity> tickets;

    public ResponseShowDto toResponseShowDto() {
        return ResponseShowDto.builder()
                .showId(this.showId)
                .filmId(this.filmId)
                .roomId(this.room.getRoomId())
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }
}
