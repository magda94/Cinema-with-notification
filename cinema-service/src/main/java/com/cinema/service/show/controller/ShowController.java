package com.cinema.service.show.controller;

import com.cinema.service.show.dto.RequestShowDto;
import com.cinema.service.show.dto.ResponseShowDto;
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
    public ResponseEntity<List<ResponseShowDto>> getAllShows() {
        log.info("Get all shows request");
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ResponseShowDto> getShowWithId(@PathVariable("showId") int showId) {
        log.info("Get show with id: '{}' request", showId);
        return ResponseEntity.ok(showService.getShowWithId(showId));
    }

    @PostMapping("")
    public ResponseEntity<ResponseShowDto> addShow(@RequestBody @Valid RequestShowDto requestShowDto) {
        log.info("Add new show with id : '{}' request", requestShowDto.getShowId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(showService.addNewShow(requestShowDto));
    }

    @DeleteMapping("/{showId}")
    public void deleteShowWithId(@PathVariable("showId") int showId) {
        log.info("Delete show with id: '{}' request", showId);
        showService.deleteShowWithId(showId);
    }
}
