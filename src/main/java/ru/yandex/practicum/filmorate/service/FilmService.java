package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       GenreStorage genreStorage,
                       RatingStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }


    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (!userStorage.checkUserExists(userId)) {
                throw new RuntimeException("юзер с id " + userId + " не найден");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId); // todo
        if (!userStorage.checkUserExists(userId)) {
            throw new RuntimeException("юзер с id " + userId + " не найден");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getMostPopular(count);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void validateFilm(Film film) {
        log.debug("проверка фильма с id {}", film.getId());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Long> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .distinct()
                    .toList();

            Set<Long> foundIds = new HashSet<>(genreStorage.findExistingGenreIds(genreIds));

            for (Long id : genreIds) {
                if (!foundIds.contains(id)) {
                    throw new NotFoundException("Жанр с id=" + id + " не найден");
                }
            }
        }

        if (film.getMpa() != null && !ratingStorage.existsById(film.getMpa().getId())) {
            throw new NotFoundException("Рейтинг с id=" + film.getMpa().getId() + " не найден");
        }
    }
}
