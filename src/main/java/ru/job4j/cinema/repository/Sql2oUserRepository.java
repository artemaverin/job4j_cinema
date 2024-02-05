package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Optional;
@Repository
public class Sql2oUserRepository implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oUserRepository.class);

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
            var textQuery = """
                    INSERT INTO users(full_name, email, password)
                    VALUES(:full_name, :email, :password)
                    """;
            var query = connection.createQuery(textQuery, true)
            .addParameter("full_name", user.getFullName())
            .addParameter("email", user.getEmail())
            .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.of(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var textQuery = """
                    SELECT * FROM users WHERE email = :email and password = :password
                    """;
            var query = connection.createQuery(textQuery);
            query.addParameter("email", email);
            query.addParameter("password", password);
            var user  = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }
}
