package com.cinema.service.ticket.entity;

import com.cinema.service.show.entity.ShowEntity;
import com.cinema.service.ticket.TicketStatus;
import com.cinema.service.ticket.dto.TicketDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "filmId", nullable = false)
    private int filmId;

    @Column(name = "userLogin")
    private String userLogin;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private ShowEntity show;

    @Embedded
    private Place place;

    public TicketDto toTicketDto() {
        return TicketDto.builder()
                .uuid(this.uuid)
                .filmId(this.filmId)
                .showId(this.show.getShowId())
                .userLogin(this.userLogin)
                .status(this.status)
                .place(this.place.toPlaceDto())
                .build();
    }
}
