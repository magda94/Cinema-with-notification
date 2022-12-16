package com.cinema.service.show.repository;

import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.entity.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<ShowEntity, Long> {

    boolean existsByShowId(int showId);

    Optional<ShowEntity> findByShowId(int showId);

    @Query("SELECT s FROM ShowEntity s " +
            " WHERE ((:startDate >= s.startDate AND :endDate <= s.endDate) OR " +
            " (:startDate <= s.startDate AND :endDate >= s.startDate) OR " +
            " (:startDate >= s.startDate AND s.endDate >= :startDate) OR " +
            " (s.startDate <= :startDate AND s.endDate >= :endDate) OR" +
            " (:startDate <= s.startDate AND :endDate >= s.endDate)) AND s.room.roomId = :roomId")
    List<ShowEntity> findOtherShow(int roomId, Instant startDate, Instant endDate);

    @Transactional
    void deleteByShowId(int showId);

    @Transactional
    void deleteAllByRoom(RoomEntity room);
}
