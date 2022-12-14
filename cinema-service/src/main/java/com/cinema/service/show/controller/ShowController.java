package com.cinema.service.show.controller;

import com.cinema.service.show.dto.ShowDto;
import com.cinema.service.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
@Slf4j
public class ShowController {

    private final ShowService showService;

    @GetMapping("")
    public ResponseEntity<List<ShowDto>> getAllShows() {
        log.info("Get all shows request");
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ShowDto> getShowWithId(@PathVariable("showId") int showId) {
        log.info("Get show with id: '{}' request", showId);
        return ResponseEntity.ok(showService.getShowWithId(showId));
    }

    @PostMapping("")
    public ResponseEntity<ShowDto> addShow(@RequestBody @Valid ShowDto showDto) {
        log.info("Add new show with id : '{}' request", showDto.getShowId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(showService.addNewShow(showDto));
    }

    @DeleteMapping("/{showId}")
    public void deleteShowWithId(@PathVariable("showId") int showId) {
        log.info("Delete show with id: '{}' request", showId);
        showService.deleteShowWithId(showId);
    }
}
