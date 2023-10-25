package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmorateApplicationUserTests {

    private UserController userController;
    private UserService userService;
    private UserStorage userStorage;
    private User user;
    private User otherUser;

    @BeforeEach
    public void init(){
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
        user = new User();
        otherUser = new User();
        //Изменяю данные первого юзера для тестов эндпоинтов
        user.setId(13);
        user.setEmail("123@ya.ru");
        user.setLogin("Vladislav");
        user.setName("vlad");
        user.setBirthday(LocalDate.of(2000,1,1));
        //Изменяю данные второго юзера для тестов эндпоинтов
        otherUser.setId(15);
        otherUser.setEmail("123@ya.ru");
        otherUser.setLogin("Vladislav");
        otherUser.setName("vlad");
        otherUser.setBirthday(LocalDate.of(2000,1,1));
    }

    @Test
    public void testUserValidation() {
        User user = new User();
        User user1 = new User();
        user1.setLogin("123");
        userService.validationName(user1);
        user.setLogin("");
        user.setBirthday(LocalDate.of(2024,1,1));
        Exception exceptionLogin = assertThrows(ValidationException.class, () -> userService.validationLogin(user));
        Exception exceptionBirthday = assertThrows(ValidationException.class, () -> userService.validationBirthday(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exceptionLogin.getMessage());
        assertEquals("Дата рождения не может быть в будущем.", exceptionBirthday.getMessage());
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void testUserCreate() {
        userController.create(user);
        assertEquals(1, userStorage.getAllUsers().size());
        userController.create(otherUser);
        assertEquals(2, userStorage.getAllUsers().size());
    }

    @Test
    public void testUserUpdate() {
        userController.create(user);
        user.setName("nickname");
        userController.update(user);
        assertEquals("nickname", userStorage.getAllUsers().get(0).getName());
    }

    @Test
    public void testUserWithWrongId() {
        userController.create(user);
        user.setId(100);
        Exception exceptionPut = assertThrows(NotFoundException.class, () -> userController.update(user));
        assertEquals("Нет пользователя с id: " + user.getId(), exceptionPut.getMessage());
    }

    @Test
    public void testGetAllUser() {
        userController.create(user);
        userController.create(otherUser);
        assertEquals(2, userStorage.getAllUsers().size());
    }

    @Test
    public void testAddAndDeleteToFriend() {
        userController.create(user);
        userController.create(otherUser);
        userController.addToFriend(13, 15);
        assertEquals(1, userController.getFriendsList(13).size());
        assertEquals(1, userController.getFriendsList(15).size());
        userController.deleteFriend(13,15);
        Exception exc = assertThrows(NotFoundException.class, () -> userController.getFriendsList(13));
        assertEquals("Список друзей пуст", exc.getMessage());
    }

    @Test
    public void testGetMutualFriendList() {
        User newUser = new User();
        newUser.setId(17);
        newUser.setEmail("123@ya.ru");
        newUser.setLogin("Vladislav");
        newUser.setName("vlad");
        newUser.setBirthday(LocalDate.of(2000,1,1));
        userController.create(newUser);
        userController.create(user);
        userController.create(otherUser);
        userController.addToFriend(13, 17);
        userController.addToFriend(15, 17);
        assertEquals(1, userController.getMutualFriendList(13, 15).size());
    }

}
