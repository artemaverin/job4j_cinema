package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {

        try (var connection = sql2o.open()) {
            var queryText = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES(:session_id, :row_number, :place_number, :user_id)
                    """;
            var query = connection.createQuery(queryText, true);
            query.addParameter("session_id", ticket.getSessionId());
            query.addParameter("row_number", ticket.getRowNumber());
            query.addParameter("place_number", ticket.getPlaceNumber());
            query.addParameter("user_id", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        }
    }
}
