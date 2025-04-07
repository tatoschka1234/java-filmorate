package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {

    FilmController controller = new FilmController();

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("test descr");
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(100);

        Exception exception = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowWhenReleaseDateTooOld() {
        Film film = new Film();
        film.setName("Old film");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.of(1700, 1, 1));
        film.setDuration(90);

        Exception exception = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertTrue(exception.getMessage().contains("Дата релиза не может быть раньше 28 декабря 1895 года"));
    }

    @Test
    void shouldThrowWhenNegDuration() {
        Film film = new Film();
        film.setName("Ok film");
        film.setDescription("Nice one");
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(-120);

        Exception exception = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertTrue(exception.getMessage().contains("Продолжительность фильма должна быть положительной"));
    }

    @Test
    void shouldThrowWhenDescriptionTooLong() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("x".repeat(201));
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(120);

        Exception exception = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertTrue(exception.getMessage().contains("Описание фильма не может превышать 200 символов"));
    }


    @Test
    void shouldPassWhenFilmIsValid() {
        Film film = new Film();
        film.setName("Ok film");
        film.setDescription("Nice one");
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(120);

        assertDoesNotThrow(() -> controller.validateFilm(film));
    }

}
