package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @PostMapping
    public User createUser(@RequestBody @Validated(OnCreate.class) User user) {
        log.info("Добавление юзера: {}", user.getName());
        validateUser(user);
        user.setId(idCounter++);
        users.put(user.getId(), user);
        log.debug("добавлен юзер с id {}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated(OnUpdate.class) User updatedUser) {
        log.info("Обновление юзера: {}", updatedUser.getName());
        long id = updatedUser.getId();
        if (!users.containsKey(id)) {
            throw new RuntimeException("Пользователь с id=" + id + " не найден");
        }
        validateUser(updatedUser);
        users.put(id, updatedUser);
        log.debug("обновлен юзер с id {}", updatedUser.getId());
        return updatedUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Получение списка всех юзеров");
        return new ArrayList<>(users.values());
    }

    public void validateUser(User user) {
        log.debug("проверка юзера с id {}", user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
