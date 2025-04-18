package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenreDbStorage.class})
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;

    @Test
    void testCreateAndGetFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(100);
        Rating rating = new Rating();
        rating.setId(1L);
        film.setMpa(rating);

        Film created = filmDbStorage.addFilm(film);
        Optional<Film> foundOpt = Optional.ofNullable(filmDbStorage.getFilm(created.getId()));

        assertThat(foundOpt)
                .isPresent()
                .hasValueSatisfying(found -> {
                    assertThat(found.getName()).isEqualTo(film.getName());
                    assertThat(found.getDescription()).isEqualTo(film.getDescription());
                    assertThat(found.getDuration()).isEqualTo(film.getDuration());
                    assertThat(found.getMpa().getId()).isEqualTo(1L);
                });
    }

    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setName("Old Film");
        film.setDescription("Old Desc");
        film.setReleaseDate(LocalDate.of(1990, 2, 2));
        film.setDuration(120);
        Rating rating = new Rating();
        rating.setId(1L);
        film.setMpa(rating);
        Film created = filmDbStorage.addFilm(film);

        created.setName("Updated Film");
        created.setDescription("Updated Desc");

        filmDbStorage.updateFilm(created);
        Optional<Film> updatedOpt = Optional.ofNullable(filmDbStorage.getFilm(created.getId()));

        assertThat(updatedOpt)
                .isPresent()
                .hasValueSatisfying(updated -> {
                    assertThat(updated.getName()).isEqualTo("Updated Film");
                    assertThat(updated.getDescription()).isEqualTo("Updated Desc");
                });
    }

    @Test
    void testGetAllFilms() {
        Film film1 = new Film();
        film1.setName("Film One");
        film1.setDescription("Desc One");
        film1.setReleaseDate(LocalDate.of(2025, 3, 3));
        film1.setDuration(90);
        Rating rating = new Rating();
        rating.setId(1L);
        film1.setMpa(rating);

        Film film2 = new Film();
        film2.setName("Film Two");
        film2.setDescription("Desc Two");
        film2.setReleaseDate(LocalDate.of(2025, 4, 4));
        film2.setDuration(110);
        Rating rating2 = new Rating();
        rating2.setId(1L);
        film2.setMpa(rating2);

        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        List<Film> films = filmDbStorage.getAllFilms();
        assertThat(films).hasSizeGreaterThanOrEqualTo(2);
    }
}
