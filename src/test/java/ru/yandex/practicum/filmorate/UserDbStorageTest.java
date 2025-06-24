package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(UserDbStorage.class)
public class UserDbStorageTest {


    private final UserDbStorage userDbStorage;

    @Test
    void testCreateAndFindUser() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userDbStorage.createUser(user);
        Optional<User> foundOpt = Optional.ofNullable(userDbStorage.getUser(created.getId()));

        assertThat(foundOpt)
                .isPresent()
                .hasValueSatisfying(found -> {
                    assertThat(found.getEmail()).isEqualTo(user.getEmail());
                    assertThat(found.getLogin()).isEqualTo(user.getLogin());
                    assertThat(found.getBirthday()).isEqualTo(user.getBirthday());
                });
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setEmail("old@email.com");
        user.setLogin("oldlogin");
        user.setName("Old Name");
        user.setBirthday(LocalDate.of(1980, 1, 1));
        User created = userDbStorage.createUser(user);

        created.setEmail("new@email.com");
        created.setName("New Name");

        userDbStorage.updateUser(created);
        Optional<User> updatedOpt = Optional.ofNullable(userDbStorage.getUser(created.getId()));

        assertThat(updatedOpt)
                .isPresent()
                .hasValueSatisfying(updated -> {
                    assertThat(updated.getEmail()).isEqualTo("new@email.com");
                    assertThat(updated.getName()).isEqualTo("New Name");
                });
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setEmail("a@email.com");
        user1.setLogin("a");
        user1.setName("A");
        user1.setBirthday(LocalDate.of(1991, 1, 1));

        User user2 = new User();
        user2.setEmail("b@email.com");
        user2.setLogin("b");
        user2.setName("B");
        user2.setBirthday(LocalDate.of(1992, 2, 2));

        userDbStorage.createUser(user1);
        userDbStorage.createUser(user2);

        List<User> all = userDbStorage.getAllUsers();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }
}
