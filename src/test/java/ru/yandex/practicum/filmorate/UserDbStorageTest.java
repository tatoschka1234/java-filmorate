package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(UserDbStorage.class)
public class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    public void testCreateAndFindUserById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userStorage.createUser(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(created.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", created.getId())
                                .hasFieldOrPropertyWithValue("email", "test@example.com")
                                .hasFieldOrPropertyWithValue("login", "testuser")
                                .hasFieldOrPropertyWithValue("name", "Test User")
                );
    }
}

