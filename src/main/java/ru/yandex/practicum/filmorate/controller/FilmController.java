package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final int maxLengthDescription = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private HashMap<Integer, Film> films = new HashMap<>();

    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    int filmId;

    private void setFilmId(Film film) {
        if (film.getId() <= 0)
            film.setId(++filmId);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Добавление фильма с id: {}", film.getId());
        validationName(film);
        validationDescription(film);
        validationReleaseDate(film);
        validationDuration(film);
        setFilmId(film);
        films.put(film.getId(), film);
        return film;

    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обновление фильма с id: {}", film.getId());
        validationName(film);
        validationDescription(film);
        validationReleaseDate(film);
        validationDuration(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Не существует фильма с id: " + film.getId());
        }
    }
    public void validationName(Film film) {

        if (film == null || film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()
                || film.getName().length() == 0) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
    }
    public void validationDescription(Film film) {

        if (film.getDescription().length() > maxLengthDescription)
            throw new ValidationException("Максимальная длина описания — 200 символов.");

    }
    public void validationReleaseDate(Film film) {

        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE))
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
    }
    public void validationDuration(Film film) {

        if (film.getDuration() <= 0)
            throw new ValidationException("Продолжительность фильма должна быть положительной.");

    }
}
