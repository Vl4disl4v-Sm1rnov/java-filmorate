package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    @JsonIgnore
    private Set<Integer> popularFilm;

    public void addLike(int id) {
        popularFilm.add(id);
    }

    public void deleteLike(int id) {
        popularFilm.remove(id);
    }

    public int getPopularFilmsSetSize(){
        return popularFilm.size();
    }

    public Film() {
        popularFilm = new HashSet<>();
    }
}
