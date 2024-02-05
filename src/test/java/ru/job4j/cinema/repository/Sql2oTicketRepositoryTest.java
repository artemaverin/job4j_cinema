package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try(var inputStream = Sql2oTicketRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    private void clearTicketsTable() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("TRUNCATE TABLE tickets RESTART IDENTITY;");
            query.executeUpdate();
        }
    }

    @Test
    public void whenSaveTicketThenGet() {
        var ticket = sql2oTicketRepository.save(new Ticket(0, 1, 1, 1, 1));
        try(var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * from tickets WHERE id =:id");
            query.addParameter("id", 1);
            var savedTicket = query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetchFirst(Ticket.class);
            assertThat(savedTicket).isEqualTo(ticket.get());
        }
    }

    @Test
    public void whenSaveSeveralThenGetCount() {
        var ticket = sql2oTicketRepository.save(new Ticket(0, 1, 1, 1, 0));
        var ticket2 = sql2oTicketRepository.save(new Ticket(1, 2, 2, 2, 0));
        try(var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM tickets");
            var rowCount = query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
            assertThat(rowCount.size()).isEqualTo(2);
        }
    }
}