package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oSessionRepositoryTest {

    private static Sql2oSessionRepository sql2oSessionRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try(var inputStream = Sql2oSessionRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oSessionRepository = new Sql2oSessionRepository(sql2o);
    }

    @Test
    public void whenFindAllSessionsThenGet() {
        var filmCollection = sql2oSessionRepository.findAll();
        assertThat(filmCollection).hasSize(2);
    }

    @Test
    public void whenFindSessionByIdThenGet(){
        var session = sql2oSessionRepository.findById(2).get();
        assertThat(session.getHallsId()).isEqualTo(2);
        assertThat(session.getFilmId()).isEqualTo(2);
    }

}