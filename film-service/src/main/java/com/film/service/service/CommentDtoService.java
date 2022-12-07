package com.film.service.service;

import com.film.service.document.CommentDocument;
import com.film.service.document.FilmDocument;
import com.film.service.dto.CommentDto;
import com.film.service.dto.ExtendCommentDto;
import com.film.service.exceptions.CommentNotFoundException;
import com.film.service.exceptions.CommentWithIdExistException;
import com.film.service.exceptions.FilmWithIdExistException;
import com.film.service.exceptions.ForbiddenParameterChangedException;
import com.film.service.repository.CommentRepository;
import com.film.service.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentDtoService {

    private final CommentRepository commentRepository;
    private final FilmRepository filmRepository;

    public List<ExtendCommentDto> getAllComments() {
        return filmRepository.findAll()
                .stream()
                .map(this::toExtendCommentDto)
                .collect(Collectors.toList());
    }

    public ExtendCommentDto getCommentsWithSortedStars(int cinemaFilmId, boolean reversed) {
        var film = filmRepository.findByCinemaFilmId(cinemaFilmId);
        if (film.isEmpty()) {
            log.error("Cannot find film with id: {}", cinemaFilmId);
            throw new FilmWithIdExistException(String.format("Cannot find film with id: %s", cinemaFilmId));
        }

        List<CommentDto> sortedComments;
        if (reversed) {
            sortedComments = getSortedComments(cinemaFilmId, Comparator.comparingInt(CommentDocument::getStars).reversed());
        } else {
            sortedComments = getSortedComments(cinemaFilmId, Comparator.comparingInt(CommentDocument::getStars));
        }

        return ExtendCommentDto.builder()
                .cinemaFilmId(cinemaFilmId)
                .filmName(film.get().getName())
                .comments(sortedComments)
                .build();
    }

    public ExtendCommentDto getCommentsWithSortedDates(int cinemaFilmId, boolean reversed) {
        var film = filmRepository.findByCinemaFilmId(cinemaFilmId);
        if (film.isEmpty()) {
            log.error("Cannot find film with id: {}", cinemaFilmId);
            throw new FilmWithIdExistException(String.format("Cannot find film with id: %s", cinemaFilmId));
        }

        List<CommentDto> sortedComments;
        if (reversed) {
            sortedComments = getSortedComments(cinemaFilmId, Comparator.comparing(CommentDocument::getCreateDate).reversed());
        } else {
            sortedComments = getSortedComments(cinemaFilmId, Comparator.comparing(CommentDocument::getCreateDate));
        }

        return ExtendCommentDto.builder()
                .cinemaFilmId(cinemaFilmId)
                .filmName(film.get().getName())
                .comments(sortedComments)
                .build();
    }

    private List<CommentDto> getSortedComments(int cinemaFilmId, Comparator<CommentDocument> action) {
        return commentRepository.findAllByCinemaFilmId(cinemaFilmId)
                .stream()
                .sorted(action)
                .map(CommentDocument::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(CommentDto commentDto) {
        if (commentRepository.existsByCinemaCommentId(commentDto.getCinemaCommentId())) {
            log.error("Comment with id: {} exists in database. Cannot add new with the same id", commentDto.getCinemaCommentId());
            throw new CommentWithIdExistException(String.format("Comment with id: %s exists in database", commentDto.getCinemaCommentId()));
        }

        if (!filmRepository.existsByCinemaFilmId(commentDto.getCinemaFilmId())) {
            log.error("Comment cannot be added due to missing film with id: {}", commentDto.getCinemaFilmId());
            throw new FilmWithIdExistException(String.format("Cannot find film with id: %s", commentDto.getCinemaFilmId()));
        }
        return commentRepository.save(toDocument(commentDto))
                .toDto();
    }

    public CommentDto updateComment(int cinemaCommentId, CommentDto commentDto) {
        var foundCommentOptional = commentRepository.findByCinemaCommentId(cinemaCommentId);
        if (foundCommentOptional.isEmpty()) {
            log.error("Cannot find comment with id: {}. Cannot update.", cinemaCommentId);
            throw new CommentNotFoundException("Cannot find comment with id: " + cinemaCommentId);
        }

        var foundDocument = foundCommentOptional.get();
        if (isAnyForbiddenParamChanged(foundDocument, commentDto)) {
            log.error("Some of forbidden parameter is changed. Update cannot be executed.");
            throw new ForbiddenParameterChangedException("Some of forbidden parameter was changed");
        }

        return commentRepository.save(toDocument(commentDto))
                .toDto();
    }

    public void deleteCommentWithId(int cinemaCommentId) {
        commentRepository.deleteByCinemaCommentId(cinemaCommentId);
    }

    public void deleteAllCommentsForFilm(int cinemaFilmId) {
        commentRepository.deleteAllByCinemaFilmId(cinemaFilmId);
    }

    private ExtendCommentDto toExtendCommentDto(FilmDocument film) {
        return ExtendCommentDto.builder()
                .cinemaFilmId(film.getCinemaFilmId())
                .filmName(film.getName())
                .comments(getCommentsForFilm(film.getCinemaFilmId()))
                .build();
    }

    private List<CommentDto> getCommentsForFilm(int cinemaFilmId) {
        return commentRepository.findAllByCinemaFilmId(cinemaFilmId)
                .stream()
                .map(CommentDocument::toDto)
                .collect(Collectors.toList());
    }

    private boolean isAnyForbiddenParamChanged(CommentDocument commentDocument, CommentDto changedComment) {
        return !commentDocument.getUserLogin().equals(changedComment.getUserLogin()) ||
                commentDocument.getCinemaCommentId() != changedComment.getCinemaCommentId() ||
                commentDocument.getCinemaFilmId() != changedComment.getCinemaFilmId() ||
                !commentDocument.getCreateDate().equals(changedComment.getCreateDate());
    }

    private CommentDocument toDocument(CommentDto commentDto) {
        return CommentDocument.builder()
                .cinemaCommentId(commentDto.getCinemaCommentId())
                .cinemaFilmId(commentDto.getCinemaFilmId())
                .userLogin(commentDto.getUserLogin())
                .createDate(commentDto.getCreateDate())
                .comment(commentDto.getComment())
                .stars(commentDto.getStars())
                .build();
    }
}
