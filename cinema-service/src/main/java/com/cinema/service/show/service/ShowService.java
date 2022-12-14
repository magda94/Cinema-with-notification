package com.cinema.service.show.service;

import com.cinema.service.exceptions.ShowExistsException;
import com.cinema.service.exceptions.ShowNotFoundException;
import com.cinema.service.room.entity.RoomEntity;
import com.cinema.service.room.repository.RoomRepository;
import com.cinema.service.show.dto.ShowDto;
import com.cinema.service.show.entity.ShowEntity;
import com.cinema.service.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowService {

    private final ShowRepository showRepository;
    private final RoomRepository roomRepository;

    public List<ShowDto> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(ShowEntity::toShowDto)
                .collect(Collectors.toList());
    }

    public ShowDto getShowWithId(int showId) {
        return showRepository.findByShowId(showId)
                .map(ShowEntity::toShowDto)
                .orElseThrow(() -> {
                    log.error("Cannot find show with id: {}", showId);
                    throw new ShowNotFoundException("Cannot find show with id: " + showId);
                });
    }

    public ShowDto addNewShow(ShowDto showDto) {
        if (showRepository.existsByShowId(showDto.getShowId())) {
            log.error("Cannot add new show. Show with id: '{}' exists in database", showDto.getShowId());
            throw new ShowExistsException(String.format("Show with id: %d exists in database", showDto.getShowId()));
        }

        var room = roomRepository.findByRoomId(showDto.getRoomId())
                .orElseThrow(() -> {
                    log.error("Cannot find room with id: {}", showDto.getRoomId());
                    throw new ShowNotFoundException("Cannot find room with id: " + showDto.getRoomId());
                });

        //TODO: add checking filmId in the future

        return showRepository.save(toEntity(showDto, room))
                .toShowDto();

    }

    public void deleteShowWithId(int showId) {
        showRepository.deleteByShowId(showId);
    }

    private ShowEntity toEntity(ShowDto showDto, RoomEntity room) {
        return ShowEntity.builder()
                .showId(showDto.getShowId())
                .filmId(showDto.getFilmId())
                .startDate(showDto.getStartDate())
                .room(room)
                .build();
    }
}
