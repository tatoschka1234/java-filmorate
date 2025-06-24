package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(RatingDbStorage.class)
public class RatingDbStorageTest {

    private final RatingDbStorage ratingDbStorage;

    @Test
    void testGetAllRatings() {
        List<Rating> ratings = ratingDbStorage.getAllRatings();
        assertThat(ratings).isNotEmpty();
    }

    @Test
    void testGetRatingById() {
        Optional<Rating> ratingOpt = Optional.ofNullable(ratingDbStorage.getRatingById(1));
        assertThat(ratingOpt)
                .isPresent()
                .hasValueSatisfying(rating -> {
                    assertThat(rating.getId()).isEqualTo(1L);
                    assertThat(rating.getName()).isNotBlank();
                });
    }

    @Test
    void testExistsById() {
        boolean exists = ratingDbStorage.existsById(1L);
        assertThat(exists).isTrue();
    }

    @Test
    void testDoesNotExistById() {
        boolean exists = ratingDbStorage.existsById(9999L);
        assertThat(exists).isFalse();
    }
}
