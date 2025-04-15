INSERT INTO ratings (code, description) VALUES
    ('G', 'Без ограничений'),
    ('PG', 'Смотреть с родителями'),
    ('PG-13', 'До 13 — нежелательно'),
    ('R', 'До 17 — только с взрослыми'),
    ('NC-17', 'До 18 — запрещено');

INSERT INTO genres (name) VALUES
  ('Комедия'),
  ('Драма'),
  ('Мульт'),
  ('Триллер'),
  ('Док'),
  ('Боевик');

INSERT INTO users (email, login, name, birthday) VALUES
 ('test1@example.com', 'qalice', 'Alice', '1995-06-01'),
 ('test2@example.com', 'bobby', 'Bob', '1978-01-15'),
 ('test3@example.com', 'ibo', 'Wang Yibo', '1985-12-10');

INSERT INTO films (name, description, release_date, duration, rating_id) VALUES
 ('Самый смешной фильм', 'типа смешно', '2020-05-10', 120, 1),
 ('Динозавры и котики', 'палео', '2018-09-01', 90, 5),
 ('Ужасы у озера', 'слэшер', '2023-02-20', 110, 4);


INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 1),
(2, 5),
(3, 4),
(3, 6);

INSERT INTO likes (film_id, user_id) VALUES
 (1, 1),
 (1, 2),
 (3, 1),
 (3, 3);


INSERT INTO friendships (user_id, friend_id, status) VALUES
 (1, 2, 'confirmed'),
 (1, 3, 'pending');
