package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {
	private FilmController filmController;
	private UserController userController;

	private User userMapping;
	private User userMapping1;
	private Film filmMapping;
	private Film filmMapping1;
	@BeforeEach
	public void beforeEach() {
		userController = new UserController();
		filmController = new FilmController();
		userMapping = new User();
		userMapping1 = new User();
		filmMapping = new Film();
		filmMapping1 = new Film();
		//Изменяю данные первого юзера для тестов эндпоинтов
		userMapping.setEmail("123@ya.ru");
		userMapping.setLogin("Vladislav");
		userMapping.setName("vlad");
		userMapping.setBirthday(LocalDate.of(2000,1,1));
		//Изменяю данные второго юзера для тестов эндпоинтов
		userMapping1.setId(1);
		userMapping1.setEmail("123@ya.ru");
		userMapping1.setLogin("Vladislav");
		userMapping1.setName("vlad");
		userMapping1.setBirthday(LocalDate.of(2000,1,1));
		//Тоже самое делаю с фильмами
		filmMapping.setName("Фильм1");
		filmMapping.setDescription("Описание1");
		filmMapping.setReleaseDate(LocalDate.of(2019,1,1));
		filmMapping.setDuration(123);
		filmMapping1.setId(0);
		filmMapping1.setName("Фильм2");
		filmMapping1.setDescription("Описание2");
		filmMapping1.setReleaseDate(LocalDate.of(1919,1,1));
		filmMapping1.setDuration(321);
	}


	@Test
	public void testFilmValidation() {
		Film film = new Film();
		film.setDescription("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescri" +
				"ptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptionde" +
				"scriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription");
		film.setReleaseDate(LocalDate.of(1800, 1,1));
		film.setDuration(-1);
		Exception exceptionName = assertThrows(ValidationException.class, () -> filmController.validationName(film));
		Exception exceptionDescription = assertThrows(ValidationException.class, () -> filmController.validationDescription(film));
		Exception exceptionReleaseDate = assertThrows(ValidationException.class, () -> filmController.validationReleaseDate(film));
		Exception exceptionDuration = assertThrows(ValidationException.class, () -> filmController.validationDuration(film));
		assertEquals("Название фильма не может быть пустым.", exceptionName.getMessage());
		assertEquals("Максимальная длина описания — 200 символов.", exceptionDescription.getMessage());
		assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exceptionReleaseDate.getMessage());
		assertEquals("Продолжительность фильма должна быть положительной.", exceptionDuration.getMessage());
	}
	@Test
	public void testUserValidation(){
		User user = new User();
		User user1 = new User();
		user1.setLogin("123");
		userController.validationName(user1);
		user.setLogin("");
		user.setBirthday(LocalDate.of(2024,1,1));
		Exception exceptionLogin = assertThrows(ValidationException.class, () -> userController.validationLogin(user));
		Exception exceptionBirthday = assertThrows(ValidationException.class, () -> userController.validationBirthday(user));
		assertEquals("Логин не может быть пустым и содержать пробелы.", exceptionLogin.getMessage());
		assertEquals("Дата рождения не может быть в будущем.", exceptionBirthday.getMessage());
		assertEquals(user1.getLogin(), user1.getName());
	}
	@Test
	public void testUserMapping(){
		//Проверка POST и GET запросов(GET проверяется в assertEquals)
		userController.create(userMapping);
		assertEquals(1,userController.findAll().size());
		userController.create(userMapping1);
		assertEquals(2,userController.findAll().size());
		//Проверка PUT запроса
		userMapping.setName("123");
		userController.update(userMapping);
		assertEquals("123", userController.getUsers().get(userMapping.getId()).getName());
		//Проверка ошибки PUT запроса
		userMapping.setId(100);
		Exception exceptionPut = assertThrows(NotFoundException.class, () -> userController.update(userMapping));
		assertEquals("Нет пользователя с id: " + userMapping.getId(), exceptionPut.getMessage());
	}
	@Test
	public void testFilmMapping(){
		//Проверка POST и GET запросов(GET проверяется в assertEquals)
		filmController.create(filmMapping);
		assertEquals(1,filmController.findAll().size());
		filmController.create(filmMapping1);
		assertEquals(2,filmController.findAll().size());
		//Проверка PUT запроса
		filmMapping.setName("Фильм100");
		filmController.update(filmMapping);
		assertEquals("Фильм100", filmController.getFilms().get(filmMapping.getId()).getName());
		//Проверка ошибки PUT запроса
		filmMapping.setId(100);
		Exception exceptionPut = assertThrows(NotFoundException.class, () -> filmController.update(filmMapping));
		assertEquals("Не существует фильма с id: " + filmMapping.getId(), exceptionPut.getMessage());
	}

}
