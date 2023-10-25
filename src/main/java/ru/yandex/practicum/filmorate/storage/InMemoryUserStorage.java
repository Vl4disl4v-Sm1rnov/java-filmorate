package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int userId;

    private HashMap<Integer, User> users = new HashMap<>();

    private void setUserId(User user) {
        if (user.getId() <= 0)
            user.setId(++userId);
    }
    
    @Override
    public User addUser(User user) {
        setUserId(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else
            throw new NotFoundException("Нет пользователя с id: " + user.getId());
    }

    @Override
    public void deleteAllUser() {
        users.clear();
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Нет пользователя с id: " + id);
        }
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
