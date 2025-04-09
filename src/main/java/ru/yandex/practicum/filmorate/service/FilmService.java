package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id);
    }
}
