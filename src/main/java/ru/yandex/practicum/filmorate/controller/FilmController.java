package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;


    @PostMapping
    public Film addFilm(@RequestBody @Validated(OnCreate.class) Film film) {
        log.info("Добавление фильма: {}", film.getName());
        validateFilm(film);
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.debug("добавлен фильм с id {}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated(OnUpdate.class) Film updatedFilm) {
        log.info("Обновление фильма: {}", updatedFilm.getName());
        validateFilm(updatedFilm);

        Long id = updatedFilm.getId();
        if (id == null || !films.containsKey(id)) {
            throw new RuntimeException("Фильм с id " + id + " не найден.");
        }

        films.put(id, updatedFilm);
        log.debug("обновлен фильм с id {}", id);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }


    public void validateFilm(Film film) {
        log.debug("проверка фильма с id {}", film.getId());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}
