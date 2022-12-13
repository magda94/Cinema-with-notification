package com.cinema.service.room.repository;

import com.cinema.service.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> findByRoomId(int roomId);

    boolean existsByRoomId(int roomId);

    @Transactional
    void deleteByRoomId(int roomId);
}
