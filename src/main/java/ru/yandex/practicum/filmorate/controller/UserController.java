package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> users = new HashMap<>();

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    private int userId;
    private void setUserId(User user){
        if(user.getId() <= 0) {
            user.setId(++userId);
        }
    }

    @GetMapping
    public List<User> findAll(){
        log.info("Количество пользователей: {}", users.size());
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        log.info("Добавление пользователя с id: {}", user.getId());
        validationLogin(user);
        validationName(user);
        validationBirthday(user);
        setUserId(user);
        users.put(user.getId(),user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        log.info("Обновление пользователя с id: {}", user.getId());
        validationLogin(user);
        validationName(user);
        validationBirthday(user);
        if(users.containsKey(user.getId())){
            users.put(user.getId(),user);
            return user;
        }else{
            throw new NotFoundException("Нет пользователя с id: " + user.getId());
        }
    }
    public void validationLogin(User user) {
        if (user.getLogin().isBlank() || user.getLogin() == null ) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
    }
    public void validationName(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
    public void validationBirthday(User user) {
        if(user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null){
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
