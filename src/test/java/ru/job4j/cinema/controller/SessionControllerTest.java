package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionControllerTest {

    private SessionService sessionService;

    private SessionController sessionController;

    @BeforeEach
    public void initServices() {
        sessionService = mock(SessionService.class);
        sessionController = new SessionController(sessionService);
    }

    @Test
    public void whenRequestSessionListPageThenGet() {
        var session1 = new SessionDto(1, "filmName", "hallsName", LocalDateTime.now(), LocalDateTime.now(), 100, 1, 1);
        var session2 = new SessionDto(2, "filmName1", "hallsName1", LocalDateTime.now(), LocalDateTime.now(), 200, 2, 2);
        var expectedSessions = List.of(session1, session2);
        when(sessionService.findAll()).thenReturn(expectedSessions);

        var model = new ConcurrentModel();
        var view = sessionController.getAll(model);
        var actualSessions = model.getAttribute("sessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualSessions).isEqualTo(expectedSessions);
    }

    @Test
    public void whenRequestSessionPageByIdAndThenGetPage() {
        int id = 5;
        var session = new SessionDto(1, "filmName", "hallsName", LocalDateTime.now(), LocalDateTime.now(), 100, 1, 1);
        var idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(sessionService.findById(idArgumentCaptor.capture())).thenReturn(Optional.of(session));

        var model = new ConcurrentModel();
        var view = sessionController.getById(model, id);
        var actualId = idArgumentCaptor.getValue();

        assertThat(view).isEqualTo("tickets/buy");
        assertThat(actualId).isEqualTo(id);
    }

    @Test
    public void whenSomeErrorWhileGetByIdThenGetErrorPageWithMessage() {
        when(sessionService.findById(anyInt())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = sessionController.getById(model, 0);
        var message = model.getAttribute("error");

        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo("Сеанс не найден");
    }

}