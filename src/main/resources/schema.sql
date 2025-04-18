CREATE TABLE IF NOT EXISTS users (
    user_id      SERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    login        VARCHAR(50)  NOT NULL UNIQUE,
    name         VARCHAR(100),
    birthday     DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id     INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    friend_id   INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    status      VARCHAR(20) NOT NULL CHECK (status IN ('confirmed', 'pending')),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    rating_id   SERIAL PRIMARY KEY,
    code        VARCHAR(10) NOT NULL UNIQUE,
    description TEXT
);


CREATE TABLE IF NOT EXISTS films (
    film_id      SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(200),
    release_date DATE NOT NULL,
    duration     INT CHECK (duration > 0),
    rating_id    INT REFERENCES ratings(rating_id)
);


CREATE TABLE IF NOT EXISTS genres (
    genre_id  SERIAL PRIMARY KEY,
    name      VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS film_genres (
    film_id   INT NOT NULL REFERENCES films(film_id) ON DELETE CASCADE,
    genre_id  INT NOT NULL REFERENCES genres(genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);


CREATE TABLE IF NOT EXISTS likes (
    film_id   INT NOT NULL REFERENCES films(film_id) ON DELETE CASCADE,
    user_id   INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);