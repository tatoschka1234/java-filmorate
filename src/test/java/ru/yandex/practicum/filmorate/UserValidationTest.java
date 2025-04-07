package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    UserController controller = new UserController();

    @Test
    void shouldThrowWhenEmailIsEmpty() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertTrue(exception.getMessage().contains("Некорректная электронная почта"));
    }

    @Test
    void shouldThrowWhenEmailMissingAtSymbol() {
        User user = new User();
        user.setEmail("user.com");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertTrue(exception.getMessage().contains("Некорректная электронная почта"));
    }

    @Test
    void shouldThrowWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("user@user.com");
        user.setLogin("user 1");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым или содержать пробелы"));
    }

    @Test
    void shouldThrowWhenLoginIsEmpty() {
        User user = new User();
        user.setEmail("user@user.com");
        user.setLogin("");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым или содержать пробелы"));
    }

    @Test
    void shouldThrowWhenBirthdayInFuture() {
        User user = new User();
        user.setEmail("user@user.com");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now().plusDays(15));

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть в будущем"));
    }

    @Test
    void shouldPassWhenUserIsValid() {
        User user = new User();
        user.setEmail("user@user.com");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 1));

        assertDoesNotThrow(() -> controller.validateUser(user));
    }
}
