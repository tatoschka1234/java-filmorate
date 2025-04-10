package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {

    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);

        userStorage.createUser(createUser(1L, "user1@email.com", "user1"));
        userStorage.createUser(createUser(2L, "user2@email.com", "user2"));

        filmStorage.addFilm(createFilm(1L, "Film1"));
        filmStorage.addFilm(createFilm(2L, "Film2"));
        filmStorage.addFilm(createFilm(3L, "Film3"));
    }

    @Test
    void shouldAddLike() {
        filmService.addLike(1L, 1L);

        Film film = filmStorage.getFilm(1L);
        assertTrue(film.getLikes().contains(1L));
    }

    @Test
    void shouldRemoveLike() {
        filmService.addLike(1L, 1L);
        filmService.removeLike(1L, 1L);

        Film film = filmStorage.getFilm(1L);
        assertFalse(film.getLikes().contains(1L));
    }

    @Test
    void shouldReturnPopularFilms() {
        filmService.addLike(1L, 1L);
        filmService.addLike(2L, 1L);
        filmService.addLike(2L, 2L);

        List<Film> popular = filmService.getPopularFilms(2);

        assertEquals(2, popular.size());
    }

    private Film createFilm(Long id, String name) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription("Description of " + name);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        return film;
    }

    private User createUser(Long id, String email, String login) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(login);
        user.setBirthday(LocalDate.of(2025, 1, 1));
        return user;
    }
}
