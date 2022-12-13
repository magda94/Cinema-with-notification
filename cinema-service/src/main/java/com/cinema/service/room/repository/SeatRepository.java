package com.cinema.service.room.repository;

import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    Set<SeatEntity> findByRoom(RoomEntity room);

    int countByRoomAndReserved(RoomEntity room, boolean reserved);

    @Transactional
    void deleteAllByRoom(RoomEntity room);
}
