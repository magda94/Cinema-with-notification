package com.cinema.service.ticket.repository;

import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.show.entity.ShowEntity;
import com.cinema.service.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<TicketEntity> findByUuid(UUID uuid);

    List<TicketEntity> findAllByFilmId(int filmId);

    @Transactional
    void deleteByUuid(UUID uuid);

    @Transactional
    void deleteAllByFilmId(int filmId);

    @Transactional
    void deleteAllByShow(ShowEntity show);
}
