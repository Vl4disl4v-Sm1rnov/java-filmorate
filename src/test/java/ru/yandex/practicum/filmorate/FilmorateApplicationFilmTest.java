package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmorateApplicationFilmTest {

    private FilmService filmService;
    private FilmController filmController;
    private FilmStorage filmStorage;
    private UserController userController;
    private UserService userService;
    private UserStorage userStorage;
    private Film film;
    private Film otherFilm;
    private User user;

    @BeforeEach
    public void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        film = new Film();
        otherFilm = new Film();
        user = new User();
        film.setName("Фильм1");
        film.setDescription("Описание1");
        film.setReleaseDate(LocalDate.of(2019,1,1));
        film.setDuration(123);
        otherFilm.setId(2);
        otherFilm.setName("Фильм2");
        otherFilm.setDescription("Описание2");
        otherFilm.setReleaseDate(LocalDate.of(1919,1,1));
        otherFilm.setDuration(321);
        user.setId(13);
        user.setEmail("123@ya.ru");
        user.setLogin("Vladislav");
        user.setName("vlad");
        user.setBirthday(LocalDate.of(2000,1,1));
    }

    @Test
    public void testFilmValidation() {
        Film film = new Film();
        film.setDescription("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescri" +
                "ptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptionde" +
                "scriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription");
        film.setReleaseDate(LocalDate.of(1800, 1,1));
        film.setDuration(-1);
        Exception exceptionName = assertThrows(ValidationException.class, () -> filmService.validationName(film));
        Exception exceptionDescription = assertThrows(ValidationException.class, () -> filmService.validationDescription(film));
        Exception exceptionReleaseDate = assertThrows(ValidationException.class, () -> filmService.validationReleaseDate(film));
        Exception exceptionDuration = assertThrows(ValidationException.class, () -> filmService.validationDuration(film));
        assertEquals("Название фильма не может быть пустым.", exceptionName.getMessage());
        assertEquals("Максимальная длина описания — 200 символов.", exceptionDescription.getMessage());
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exceptionReleaseDate.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", exceptionDuration.getMessage());
    }

    @Test
    public void testFilmCreate() {
        filmController.create(film);
        assertEquals(1, filmStorage.getAllFilms().size());
        filmController.create(otherFilm);
        assertEquals(2, filmStorage.getAllFilms().size());
    }

    @Test
    public void testFilmUpdate() {
        filmController.create(film);
        film.setName("film name");
        assertEquals("film name", filmStorage.getAllFilms().get(0).getName());
    }

    @Test
    public void testFilmWithWrongId() {
        filmController.create(film);
        film.setId(1000);
        Exception exceptionPut = assertThrows(NotFoundException.class, () -> filmController.update(film));
        assertEquals("Не существует фильма с id: " + film.getId(), exceptionPut.getMessage());
    }

    @Test
    public void testGetAllFilms() {
        filmController.create(film);
        filmController.create(otherFilm);
        assertEquals(2, filmStorage.getAllFilms().size());
    }

    @Test
    public void testAddAndDeleteLikeToFilm() {
        userController.create(user);
        filmController.create(film);
        filmController.addLikeForFilm(1, 13);
        assertEquals(1, film.getPopularFilmsSetSize());
        filmController.deleteLikeForFilm(1,13);
        assertEquals(0, film.getPopularFilmsSetSize());
    }

    @Test
    public void testGetPopularFilms() {
        userController.create(user);
        filmController.create(film);
        filmController.addLikeForFilm(1,13);
        assertEquals(film.getName(), filmController.getPopularFilms(10).get(0).getName());
    }
}
