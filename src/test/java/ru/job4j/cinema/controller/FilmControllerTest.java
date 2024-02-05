package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmService filmService;

    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmListPageThenGet() {
        var film1 = new FilmDto(1, "name", "description", 1990, 1, 12, "genre", 1);
        var film2 = new FilmDto(2, "name", "description", 1990, 2, 13, "genre2", 2);
        var expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

    @Test
    public void whenRequestFilmPageByIdAndThenGetPage() {
        int id = 5;
        var candidate = new FilmDto(1, "name", "description", 1990, 1, 12, "genre", 1);
        var idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(filmService.findById(idArgumentCaptor.capture())).thenReturn(Optional.of(candidate));

        var model = new ConcurrentModel();
        var view = filmController.getById(model, id);
        var actualId = idArgumentCaptor.getValue();

        assertThat(view).isEqualTo("films/one");
        assertThat(actualId).isEqualTo(id);
    }

    @Test
    public void whenSomeErrorWhileGetByIdThenGetErrorPageWithMessage() {
        when(filmService.findById(anyInt())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = filmController.getById(model, 0);
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo("Фильма с указанными id не сущестует");
    }

}