package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long userId, Long friendId, String status) {
        String sql = "INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, status);
    }

    @Override
    public boolean exists(Long userId, Long friendId) {
        String sql = "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, friendId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsWithStatus(Long userId, Long friendId, String status) {
        String sql = "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ? AND status = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, friendId, status);
        return count != null && count > 0;
    }

    @Override
    public void updateStatus(Long userId, Long friendId, String status) {
        String sql = "UPDATE friendships SET status = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, status, userId, friendId);
    }

    @Override
    public void remove(Long userId, Long friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        String sql = """
        SELECT u.*
        FROM users AS u
        INNER JOIN friendships AS f1 ON u.user_id = f1.friend_id
        INNER JOIN friendships AS f2 ON u.user_id = f2.friend_id
        WHERE f1.user_id = ? AND f2.user_id = ?
    """;

        return jdbcTemplate.query(sql, new UserRowMapper(), userId, otherUserId);
    }
}
