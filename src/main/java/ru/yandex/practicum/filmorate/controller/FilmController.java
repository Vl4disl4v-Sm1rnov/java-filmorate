package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.info("Количество фильмов: {}", filmService.getFilms().size());
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Фильм с id: {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Количество популярных фильмов: {}", count);
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeForFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с id: {}, поставил лайк фильму с id: {}", userId, id);
        filmService.addLikeForFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeForFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с id: {}, удалил лайк с фильма: {}", userId, id);
        filmService.deleteLikeForFilm(id, userId);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Добавление фильма с id: {}", film.getId());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обновление фильма с id: {}", film.getId());
        return filmService.update(film);
    }

}
