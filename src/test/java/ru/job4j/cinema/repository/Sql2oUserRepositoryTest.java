package ru.job4j.cinema.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    private void clearUsersTable() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("TRUNCATE TABLE users RESTART IDENTITY;");
            query.executeUpdate();
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "name", "email", "password")).get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(savedUser.get()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        sql2oUserRepository.save(new User(0, "email1", "name1", "password1"));
        sql2oUserRepository.save(new User(0, "email2", "name2", "password2"));
        sql2oUserRepository.save(new User(0, "email3", "name3", "password3"));
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            var rowCount = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
            Assertions.assertThat(rowCount.size()).isEqualTo(3);
        }
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            var rowCount = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
            assertThat(sql2oUserRepository.findByEmailAndPassword("email", "password")).isEqualTo(empty());
        }

    }

    @Test
    public void whenSameEmailAdd() {
        var user = new User(0, "email", "name1", "password1");
        var userSaved = sql2oUserRepository.save(user);
        var user2 = new User(0, "email", "name2", "password2");
        assertThat(userSaved.get()).usingRecursiveComparison().isEqualTo(user);
        assertThat(sql2oUserRepository.findByEmailAndPassword(user2.getEmail(), user2.getPassword())).isEqualTo(empty());
    }

}