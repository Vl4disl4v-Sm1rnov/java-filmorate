package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    int id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Integer> friends;

    public User() {
        friends = new HashSet<>();
    }
}
