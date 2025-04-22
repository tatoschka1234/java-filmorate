package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;


@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;


    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }


    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        return friendshipStorage.getCommonFriends(userId, otherUserId);
    }


    public User createUser(User user) {
        validateUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User updatedUser) {
        validateUser(updatedUser);
        return userStorage.updateUser(updatedUser);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public void validateUser(User user) {
        log.debug("проверка юзера с id {}", user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void addFriend(Long userId, Long friendId) {
        if (!userStorage.checkUserExists(userId) || !userStorage.checkUserExists(friendId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }

        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }

        if (friendshipStorage.exists(userId, friendId)) {
            throw new ValidationException("Запись о дружбе уже существует");
        }

        boolean reverseExists = friendshipStorage.exists(friendId, userId);
        if (reverseExists) {
            friendshipStorage.add(userId, friendId, "confirmed");
            friendshipStorage.updateStatus(friendId, userId, "confirmed");
        } else {
            friendshipStorage.add(userId, friendId, "pending");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        if (!userStorage.checkUserExists(userId) || !userStorage.checkUserExists(friendId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        friendshipStorage.remove(userId, friendId);
        if (friendshipStorage.exists(friendId, userId)) {
            friendshipStorage.updateStatus(friendId, userId, "pending");
        }
    }

    public List<User> getFriends(Long userId) {
        if (!userStorage.checkUserExists(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return userStorage.getFriends(userId);
    }


}



