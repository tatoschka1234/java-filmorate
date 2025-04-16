package ru.yandex.practicum.filmorate.storage;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;

public interface UserStorage {
    User createUser(@RequestBody @Validated(OnCreate.class) User user);

    User updateUser(@RequestBody @Validated(OnUpdate.class) User updatedUser);

    List<User> getAllUsers();

    User getUser(Long id);

    boolean checkUserExists(Long id);
    List<User> getConfirmedFriends(Long userId);
    List<User> getFriends(Long userId);
}
