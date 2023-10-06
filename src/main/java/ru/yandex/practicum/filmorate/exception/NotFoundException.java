package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends NullPointerException {
    public NotFoundException(String s) {
        super(s);
    }
}
