package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private TicketService ticketService;

    private SessionService sessionService;

    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        sessionService = mock(SessionService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenPostTicketThenSameDataAndReturnSuccessPage() throws Exception {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketArgumentCaptor.capture())).thenReturn(Optional.of(ticket));

        var model = new ConcurrentModel();
        var view = ticketController.create(ticket, model);
        var actualVacancy = ticketArgumentCaptor.getValue();

        assertThat(view).isEqualTo("tickets/success");
        assertThat(actualVacancy).isEqualTo(ticket);
    }

    @Test
    public void whenSomeExceptionThrownThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Билет на эти места уже забронирован");
        when(ticketService.save(any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var view = ticketController.create(ticket, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("tickets/reject");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

}