package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUser(userId);
        return user.getFriends().stream()
                .map(userStorage::getUser)
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.getUser(userId);
        User other = userStorage.getUser(otherId);

        Set<Long> common = new HashSet<>(user.getFriends());
        common.retainAll(other.getFriends());

        return common.stream()
                .map(userStorage::getUser)
                .toList();
    }
}
