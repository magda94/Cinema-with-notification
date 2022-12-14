package com.cinema.service.show.repository;

import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.entity.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<ShowEntity, Long> {

    boolean existsByShowId(int showId);

    Optional<ShowEntity> findByShowId(int showId);

    @Transactional
    void deleteByShowId(int showId);

    @Transactional
    void deleteAllByRoom(RoomEntity room);
}
