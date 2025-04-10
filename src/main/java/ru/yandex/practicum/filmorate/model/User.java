package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Null(groups = OnCreate.class, message = "ID не должен передаваться при создании")
    @NotNull(groups = OnUpdate.class, message = "ID обязателен при обновлении")
    private Long id;

    @NotBlank(message = "Электронная почта не может быть пустой", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Электронная почта должна содержать символ @", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "Логин не может быть пустым", groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы", groups = {OnCreate.class, OnUpdate.class})
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
