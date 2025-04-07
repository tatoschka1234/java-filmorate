package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавление фильма: {}", film.getName());
        validateFilm(film);
        film.setId(idCounter++);
        films.add(film);
        log.debug("добавлен фильм с id {}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        log.info("Обновление фильма: {}", updatedFilm.getName());
        validateFilm(updatedFilm);
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == updatedFilm.getId()) {
                films.set(i, updatedFilm);
                log.debug("обновлен фильм с id {}", updatedFilm.getId());
                return updatedFilm;
            }
        }
        throw new RuntimeException("Фильм с id " + updatedFilm.getId() + " не найден.");
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Ролучение списка всех фильмов");
        return films;
    }

    public void validateFilm(Film film) {
        log.trace("проверка фильмф с id {}", film.getId());
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
