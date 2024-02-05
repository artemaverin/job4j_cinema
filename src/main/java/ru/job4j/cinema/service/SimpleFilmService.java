package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, GenreRepository sql2oGenreRepository) {
        this.filmRepository = sql2oFilmRepository;
        this.genreRepository = sql2oGenreRepository;
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        Optional<FilmDto> filmDto = Optional.empty();
        for (FilmDto film: findAll()) {
            if (film.getId() == id) {
                filmDto = Optional.of(film);
                break;
            }
        }
        return filmDto;
    }

    @Override
    public Collection<FilmDto> findAll() {
        Collection<FilmDto> filmDtoCollection = new ArrayList<>();
        for (Film film:filmRepository.findAll()) {
            var genre = genreRepository.findById(film.getGenreId());
            var filmDto = new FilmDto(
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getYear(),
                    film.getMinimalAge(),
                    film.getDurationInMinutes(),
                    genre.getName(),
                    film.getFileId());
            filmDtoCollection.add(filmDto);
        }
        return filmDtoCollection;
    }
}
