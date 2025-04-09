package ru.yandex.practicum.filmorate.storage;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;

public interface FilmStorage {
    Film addFilm(@RequestBody @Validated(OnCreate.class) Film film);
    Film updateFilm(@RequestBody @Validated(OnUpdate.class) Film updatedFilm);
    List<Film> getAllFilms();
    Film getFilm(Long id);
}
