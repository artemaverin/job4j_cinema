package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleSessionService implements SessionService {

    private final SessionRepository sessionRepository;

    private final FilmRepository filmRepository;

    private final HallRepository hallRepository;

    public SimpleSessionService(SessionRepository sql2oSessionRepository, FilmRepository sql2oFilmRepository, HallRepository sql2oHallRepository) {
        this.sessionRepository = sql2oSessionRepository;
        this.filmRepository = sql2oFilmRepository;
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<SessionDto> findById(int id) {
        Optional<SessionDto> sessionDto = Optional.empty();
        for (SessionDto session: findAll()) {
            if (session.getId() == id) {
                sessionDto = Optional.of(session);
            }
        }
        return sessionDto;
    }

    @Override
    public Collection<SessionDto> findAll() {
        Collection<SessionDto> sessionDtoCollection = new ArrayList<>();
        for (Session session:sessionRepository.findAll()) {
            var filmName = filmRepository.findById(session.getFilmId());
            var hallName = hallRepository.findById(session.getHallsId());
            var sessionDto = new SessionDto(
                    session.getId(),
                    filmName.get().getName(),
                    hallName.getName(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getPrice(),
                    hallName.getRowCount(),
                    hallName.getPlaceCount()
            );
            sessionDtoCollection.add(sessionDto);
        }
        return sessionDtoCollection;
    }
}
