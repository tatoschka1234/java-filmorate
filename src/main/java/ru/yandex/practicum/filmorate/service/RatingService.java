package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingDbStorage ratingStorage;

    public List<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    public Rating getRatingById(int id) {
        return ratingStorage.getRatingById(id);
    }
}

