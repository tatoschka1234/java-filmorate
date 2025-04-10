package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private InMemoryUserStorage userStorage;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);

        userStorage.createUser(createUser(1L, "user1@email.com", "user1"));
        userStorage.createUser(createUser(2L, "user2@email.com", "user2"));
        userStorage.createUser(createUser(3L, "user3@email.com", "user3"));
    }

    @Test
    void shouldRemoveFriendCorrectly() {
        userService.addFriend(1L, 2L);
        userService.removeFriend(1L, 2L);

        User user1 = userStorage.getUser(1L);
        User user2 = userStorage.getUser(2L);

        assertFalse(user1.getFriends().contains(2L));
        assertFalse(user2.getFriends().contains(1L));
    }

    @Test
    void shouldReturnCommonFriends() {
        userService.addFriend(1L, 3L);
        userService.addFriend(2L, 3L);

        List<User> commonFriends = userService.getCommonFriends(1L, 2L);

        assertEquals(1, commonFriends.size());
        assertEquals(3L, commonFriends.get(0).getId());
    }

    private User createUser(Long id, String email, String login) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(login);
        user.setBirthday(LocalDate.of(2025, 1, 1));
        return user;
    }

    @Test
    void shouldAddFriendsCorrectly() {
        userService.addFriend(1L, 2L);

        User user1 = userStorage.getUser(1L);
        User user2 = userStorage.getUser(2L);

        assertTrue(user1.getFriends().contains(2L), "User1 должен видеть User 2 в друзьях");
        assertTrue(user2.getFriends().contains(1L), "User 2 должен видеть User 1 в друзьях");
    }

    @Test
    void shouldNotDuplicateFriends() {
        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 2L);

        User user1 = userStorage.getUser(1L);

        assertEquals(1, user1.getFriends().size(), "Не должно быть дубликатов");
    }


}
