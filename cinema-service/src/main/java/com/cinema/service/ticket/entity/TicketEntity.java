package com.cinema.service.ticket.entity;

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

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "filmId", nullable = false)
    private int filmId;

    @Column(name = "userLogin")
    private String userLogin;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    public TicketDto toTicketDto() {
        return TicketDto.builder()
                .uuid(this.uuid)
                .filmId(this.filmId)
                .userLogin(this.userLogin)
                .status(this.status)
                .build();
    }
}
