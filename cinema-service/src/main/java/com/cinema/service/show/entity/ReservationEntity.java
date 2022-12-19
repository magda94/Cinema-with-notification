package com.cinema.service.show.entity;

import com.cinema.service.show.dto.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private UUID ticketId;

    private int showId;

    private String userLogin;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
