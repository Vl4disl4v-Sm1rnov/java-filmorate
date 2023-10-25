package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage us;


    public List<User> getUsers() {
        log.info("Количество пользователей: {}", us.getAllUsers().size());
        return us.getAllUsers();
    }

    public User create(User user) {
        log.info("Добавление пользователя с id: {}", user.getId());
        validationLogin(user);
        validationName(user);
        validationBirthday(user);
        return us.addUser(user);
    }

    public User update(User user) {
        log.info("Обновление пользователя с id: {}", user.getId());
        validationLogin(user);
        validationName(user);
        validationBirthday(user);
        return us.updateUser(user);
    }

    public User getUserById(int id) {
        User user = us.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Несуществует пользователя с id: " + id);
        }
        return user;
    }

    public void addToFriend(int id, int friendId) {
        User user = us.getUserById(id);
        User friend = us.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        User user = us.getUserById(id);
        User friend = us.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> getMutualList(int id, int otherId) {
        User user = us.getUserById(id);
        User otherUser = us.getUserById(otherId);
        List<User> mutualFriends = new ArrayList<>();
        for (Integer friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                mutualFriends.add(us.getUserById((friend)));
            }
        }
        log.info("Получение списка общих друзей");
        return mutualFriends;
    }

    public List<User> getFriendList(int id) {
        User user = us.getUserById(id);
        List<User> friendsList = new ArrayList<>();
        Set<Integer> friends = user.getFriends();
        if(friends.isEmpty()) {
            throw new NotFoundException("Список друзей пуст");
        }
        for (Integer friendId : friends) {
            User friend = us.getUserById(friendId);
            if (friend != null){
                friendsList.add(friend);
            }
        }
        log.info("Получение списка друзей");
        return friendsList;
    }

    public void validationLogin(User user) {
        if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
    }

    public void validationName(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public void validationBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
