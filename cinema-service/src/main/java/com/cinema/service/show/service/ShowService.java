package com.cinema.service.show.service;

import com.cinema.service.show.dto.ShowDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    public List<ShowDto> getAllShows() {
        return List.of();
    }

    public ShowDto getShowWithId(int showId) {
        return null;
    }

    public ShowDto addNewShow(ShowDto showDto) {
        return null;
    }

    public void deleteShowWithId(int showId) {

    }
}
