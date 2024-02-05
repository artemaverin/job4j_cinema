package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
public interface TicketRepository {
    Optional<Ticket> save(Ticket ticket);
}
