package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FriendshipDbStorage.class})
public class FriendshipDbStorageTest {

    private final UserDbStorage userStorage;
    private final FriendshipDbStorage friendshipStorage;
    private final JdbcTemplate jdbcTemplate;

    private Long userId1;
    private Long userId2;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM friendships");
        jdbcTemplate.update("DELETE FROM users");

        User user1 = new User();
        user1.setEmail("a@email.com");
        user1.setLogin("alice");
        user1.setName("Alice");
        user1.setBirthday(LocalDate.of(1999, 1, 1));
        userId1 = userStorage.createUser(user1).getId();

        User user2 = new User();
        user2.setEmail("b@email.com");
        user2.setLogin("bob");
        user2.setName("Bob");
        user2.setBirthday(LocalDate.of(1991, 2, 2));
        userId2 = userStorage.createUser(user2).getId();
    }

    @Test
    void testAddAndExists() {
        friendshipStorage.add(userId1, userId2, "pending");

        boolean exists = friendshipStorage.exists(userId1, userId2);
        assertThat(exists).isTrue();

        boolean confirmed = friendshipStorage.existsWithStatus(userId1, userId2, "confirmed");
        assertThat(confirmed).isFalse();
    }

    @Test
    void testUpdateStatus() {
        friendshipStorage.add(userId1, userId2, "pending");
        friendshipStorage.updateStatus(userId1, userId2, "confirmed");

        boolean confirmed = friendshipStorage.existsWithStatus(userId1, userId2, "confirmed");
        assertThat(confirmed).isTrue();
    }

    @Test
    void testRemove() {
        friendshipStorage.add(userId1, userId2, "pending");
        friendshipStorage.remove(userId1, userId2);

        boolean exists = friendshipStorage.exists(userId1, userId2);
        assertThat(exists).isFalse();
    }

    @Test
    void testGetCommonFriends() {
        User common = new User();
        common.setEmail("c@email.com");
        common.setLogin("common");
        common.setName("Common");
        common.setBirthday(LocalDate.of(1995, 5, 5));
        Long commonId = userStorage.createUser(common).getId();

        friendshipStorage.add(userId1, commonId, "confirmed");
        friendshipStorage.add(userId2, commonId, "confirmed");

        List<User> commonFriends = friendshipStorage.getCommonFriends(userId1, userId2);
        assertThat(commonFriends).hasSize(1);
        assertThat(commonFriends.get(0).getId()).isEqualTo(commonId);
    }
}
