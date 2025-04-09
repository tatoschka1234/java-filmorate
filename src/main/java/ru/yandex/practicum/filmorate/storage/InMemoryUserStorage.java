package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User createUser(User user) {
        log.info("Добавление юзера: {}", user.getName());
        user.setId(idCounter++);
        users.put(user.getId(), user);
        log.debug("добавлен юзер с id {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        log.info("Обновление юзера: {}", updatedUser.getName());
        long id = updatedUser.getId();
        if (!users.containsKey(id)) {
            throw new RuntimeException("Пользователь с id=" + id + " не найден");
        }

        users.put(id, updatedUser);
        log.debug("обновлен юзер с id {}", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Получение списка всех юзеров");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new RuntimeException("юзер с id " + id + " не найден.");
        }
        return user;
    }
}
