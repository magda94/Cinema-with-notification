package com.cinema.service.room.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
