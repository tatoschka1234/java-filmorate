package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    @Override
    public Film addFilm(Film film) {
        log.info("Добавление фильма: {}", film.getName());
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.debug("добавлен фильм с id {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        log.info("Обновление фильма: {}", updatedFilm.getName());

        Long id = updatedFilm.getId();
        if (id == null || !films.containsKey(id)) {
            throw new RuntimeException("Фильм с id " + id + " не найден.");
        }

        films.put(id, updatedFilm);
        log.debug("обновлен фильм с id {}", id);
        return updatedFilm;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new RuntimeException("Фильм с id " + id + " не найден");
        }
        return film;
    }

    public List<Film> getMostPopular(int count) {
        return films.values().stream()
                .peek(f -> System.out.println(f.getClass()))
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (film != null) {
            film.getLikes().add(userId);
        } else {
            throw new RuntimeException("Фильм с id=" + filmId + " не найден");
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (film != null) {
            film.getLikes().remove(userId);
        } else {
            throw new RuntimeException("Фильм с id=" + filmId + " не найден");
        }
    }

}
