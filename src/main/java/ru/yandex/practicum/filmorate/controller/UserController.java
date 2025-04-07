package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Добавление юзера: {}", user.getName());
        validateUser(user);
        user.setId(idCounter++);
        users.add(user);
        log.debug("добавлен юзер с id {}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Обновление юзера: {}", updatedUser.getName());
        validateUser(updatedUser);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                log.debug("обновлен юзер с id {}", updatedUser.getId());
                return updatedUser;
            }
        }
        throw new RuntimeException("юзер с id=" + updatedUser.getId() + " не найден");
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Получение списка всех юзеров");
        return users;
    }

    public void validateUser(User user) {
        log.trace("проверка юзера с id {}", user.getId());
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректная электронная почта");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
