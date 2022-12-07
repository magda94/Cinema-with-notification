package com.film.service.controllers;

import com.film.service.dto.CommentDto;
import com.film.service.dto.ExtendCommentDto;
import com.film.service.service.CommentDtoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentDtoController {

    private final CommentDtoService commentDtoService;

    @GetMapping("")
    public ResponseEntity<List<ExtendCommentDto>> getAllComments() {
        log.info("Get all comments request");
        return ResponseEntity.ok(commentDtoService.getAllComments());
    }
    
    @GetMapping("/{cinemaFilmId}/dateSorted")
    public ResponseEntity<ExtendCommentDto> getCommentsForFilmDateSorted(@PathVariable("cinemaFilmId") int cinemaFilmId, boolean reversed) {
        log.info("Get sorted by date comments, reversed : {}, for film with id: {}", reversed, cinemaFilmId);
        return ResponseEntity.ok(commentDtoService.getCommentsWithSortedDates(cinemaFilmId, reversed));
    }

    @GetMapping("/{cinemaFilmId}/starsSorted")
    public ResponseEntity<ExtendCommentDto> getCommentForFilmWithSortedStars(@PathVariable("cinemaFilmId") int cinemaFilmId, boolean reversed) {
        log.info("Get sorted by star comments, reserved: {}, for film with id: {}", reversed, cinemaFilmId);
        return ResponseEntity.ok(commentDtoService.getCommentsWithSortedStars(cinemaFilmId, reversed));
    }

    @PostMapping("")
    public ResponseEntity<CommentDto> addComment(@RequestBody @Valid CommentDto commentDto) {
        log.info("Create new comment with cinemaCommentId: {} for film : {} request",
                commentDto.getCinemaCommentId(), commentDto.getCinemaFilmId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentDtoService.addComment(commentDto));
    }

    @PutMapping("{cinemaCommentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("cinemaCommentId") int cinemaCommentId, @RequestBody CommentDto commentDto) {
        log.info("Update comment with cinameCommentId: {} request");
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(commentDtoService.updateComment(cinemaCommentId, commentDto));
    }

    @DeleteMapping("/{cinemaCommentId}")
    public void deleteCommentWithId(@PathVariable("cinemaCommentId") int cinemaCommentId) {
        log.info("Delete comment with cinemaCommentId: {} request", cinemaCommentId);
        commentDtoService.deleteCommentWithId(cinemaCommentId);
    }

    @DeleteMapping("/film/{cinemaFilmId}")
    public void deleteAllCommentsForFilm(@PathVariable("cinemaFilmId") int cinemaFilmId) {
        log.info("Delete all comments for film with cinemaFilmId: {} request", cinemaFilmId);
        commentDtoService.deleteAllCommentsForFilm(cinemaFilmId);
    }
}
