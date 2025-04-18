package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;


    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                ps.setLong(5, film.getMpa().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);
        insertGenres(id, film.getGenres());
        return film;
    }


    @Override
    public Film updateFilm(Film updatedFilm) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE film_id = ?";
        int rows = jdbcTemplate.update(sql,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa() != null ? updatedFilm.getMpa().getId() : null,
                updatedFilm.getId());

        if (rows == 0) {
            throw new RuntimeException("Фильм с id=" + updatedFilm.getId() + " не найден.");
        }

        deleteGenres(updatedFilm.getId());
        insertGenres(updatedFilm.getId(), updatedFilm.getGenres());
        return updatedFilm;
    }


    public List<Film> getAllFilms() {
        String sql = """
                SELECT f.*, r.rating_id AS mpa_id, r.code AS mpa_name
                FROM films f
                LEFT JOIN ratings r ON f.rating_id = r.rating_id
                """;

        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());
        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .toList();

        Map<Long, Set<Genre>> genresMap = genreStorage.getGenresForFilms(filmIds);
        for (Film film : films) {
            film.setGenres(genresMap.getOrDefault(film.getId(), new HashSet<>()));
        }

        return films;
    }


    @Override
    public Film getFilm(Long id) {
        String sql = """
                    SELECT f.*, r.rating_id AS mpa_id, r.code AS mpa_name
                    FROM films f
                    LEFT JOIN ratings r ON f.rating_id = r.rating_id
                    WHERE f.film_id = ?
                """;

        Film film = jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
        Map<Long, Set<Genre>> genreMap = genreStorage.getGenresForFilms(List.of(id));
        film.setGenres(genreMap.getOrDefault(id, new HashSet<>()));

        return film;
    }


    @Override
    public List<Film> getMostPopular(int count) {
        String sql = """
                    SELECT f.*, r.rating_id AS mpa_id, r.code AS mpa_name
                    FROM films f
                    LEFT JOIN ratings r ON f.rating_id = r.rating_id
                    LEFT JOIN (
                        SELECT film_id, COUNT(user_id) AS like_count
                        FROM likes
                        GROUP BY film_id
                    ) AS l ON f.film_id = l.film_id
                    ORDER BY COALESCE(l.like_count, 0) DESC
                    LIMIT ?
                """;

        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper(), count);

        List<Long> filmIds = films.stream().map(Film::getId).toList();
        Map<Long, Set<Genre>> genresMap = genreStorage.getGenresForFilms(filmIds);

        for (Film film : films) {
            film.setGenres(genresMap.getOrDefault(film.getId(), new HashSet<>()));
        }

        return films;
    }

    private void insertGenres(Long filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;

        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, genres, genres.size(), (ps, genre) -> {
            ps.setLong(1, filmId);
            ps.setLong(2, genre.getId());
        });
    }


    private void deleteGenres(Long filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public boolean checkFilmExists(Long id) {
        String sql = "SELECT COUNT(*) FROM films WHERE film_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }


}

