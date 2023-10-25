package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Количество пользователей: {}", userService.getUsers().size());
        return userService.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Добавление пользователя с id: {}", user.getId());
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя с id: {}", user.getId());
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Пользователь с id: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        log.info("Список друзей пользователя с id: ", id);
        return userService.getFriendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriendList(@PathVariable int id, @PathVariable int otherId) {
        log.info("Список общих друзей пользователей: {}, {}", id, otherId);
        return userService.getMutualList(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriend (@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id: {}, добавляет в друзья пользователя с id: {}", id, friendId);
        userService.addToFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id: {}, удаляет из друзей пользователя с id: {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }
}
