package com.cinema.service.show.repository;

import com.cinema.service.show.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {

    @Query("SELECT r FROM ReservationEntity r WHERE r.showId = :showId " +
            " AND r.status != 'CANCELLED'")
    List<ReservationEntity> findAllActualByShowId(int showId);
}
