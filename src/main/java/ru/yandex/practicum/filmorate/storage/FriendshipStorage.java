package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void add(Long userId, Long friendId, String status);
    boolean exists(Long userId, Long friendId);
    boolean existsWithStatus(Long userId, Long friendId, String status);
    void updateStatus(Long userId, Long friendId, String status);
    void remove(Long userId, Long friendId);
    List<User> getCommonFriends(Long userId, Long otherUserId);
}


