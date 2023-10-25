package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;
    private final int maxLengthDescription = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);


    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        if(film != null) {
            return film;
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }


    public Film create(Film film) {
        log.info("Добавление фильма с id: {}", film.getId());
        validationName(film);
        validationDescription(film);
        validationReleaseDate(film);
        validationDuration(film);
        return filmStorage.addFilm(film);

    }

    public Film update(Film film) {
        log.info("Обновление фильма с id: {}", film.getId());
        validationName(film);
        validationDescription(film);
        validationReleaseDate(film);
        validationDuration(film);
        return filmStorage.updateFilm(film);
    }

    public void addLikeForFilm(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
    }

    public void deleteLikeForFilm(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        film.deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count){
        return filmStorage.getPopularFilms(count);
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
