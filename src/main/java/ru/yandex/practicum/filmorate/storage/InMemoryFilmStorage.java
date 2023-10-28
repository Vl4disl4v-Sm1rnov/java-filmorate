package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Integer, Film> films = new HashMap<>();

    int filmId;

    private void setFilmId(Film film) {
        if (film.getId() <= 0)
            film.setId(++filmId);
    }

    @Override
    public Film addFilm(Film film) {
        setFilmId(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Не существует фильма с id: " + film.getId());
        }
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(Film::getPopularFilmsSetSize).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Нет фильма с id: " + id);
        }
    }
}
