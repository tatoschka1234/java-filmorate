package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    @Null(groups = OnCreate.class, message = "ID не должен передаваться при создании")
    @NotNull(groups = OnUpdate.class, message = "ID обязателен при обновлении")
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Size(max = 200, message = "Описание фильма не может превышать 200 символов", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "Дата релиза обязательна", groups = {OnCreate.class, OnUpdate.class})
    @PastOrPresent(message = "Дата не может быть в будущем", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной", groups = {OnCreate.class, OnUpdate.class})
    private Integer duration;
}
