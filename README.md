# java-filmorate
https://dbdiagram.io/d/65479a1d7d8bbd646583dc3e - ссылка на диаграмму
![Untitled](https://github.com/Vl4disl4v-Sm1rnov/java-filmorate/assets/120194556/0bbd4eb1-9cca-460f-9b0b-fe2235cbeb47)
Скрипт написания диаграммы:
```
Table film {
  film_id int PK
  name varchar
  description varchar
  releaseDate LocalDate
  duration int
  mpa_id int 
  
}

Table likes{
  film_id int PK
  user_id int PK
}
Table filmGenres{
  film_id int PK
  genre_id int PK
}
Table genre{
  genre_id int PK
  genre varchar
}
Table mpa{
  mpa_id int PK
  mpa varchar
}
Table user{
  user_id int PK
  email varchar
  login varchar
  name varchar
  birthday LocalDate
}
Table friends{
  user_id int PK
  related_id int PK
  is_confirm boolean 
}

Ref: film.film_id < filmGenres.film_id 
Ref: film.film_id < likes.film_id
Ref: film.mpa_id > mpa.mpa_id
Ref: filmGenres.genre_id > genre.genre_id
Ref: likes.user_id > user.user_id
Ref: user.user_id < friends.user_id
Ref: user.user_id < friends.related_id
```

Диаграмма описывает структуру базы данных, которая может использоваться в приложении "java-filmorate".

Таблица film содержит информацию о фильмах, такую как: id фильма(film_id), название(name), описание(description), дату релиза(releaseDate), продолжительность(duration) и идентификатор mpa(mpa_id).

Таблица likes содержит информацию о пользователях, которые поставили свои лайки какому то фильму. Таблица likes связана с таблицей film по id фильма(film_id), с таблицей user по id пользователя(user_id).

Таблица mpa содержит информацию о возрастном рейтинге фильмов в поле mpa. Таблица mpa связана с таблицей film по идентификатору mpa(mpa_id).

Таблица filmGenres это связующая таблица между таблицами film(по film_id) и genre(по genre_id).

Таблица genre содержит информацию о жанре фильма(genre) и связана с таблицей filmGenres по genre_id.

Таблица user содержит информацию о пользователях, такую как: id пользователя(user_id), адрес электронной почты(email), логин пользователя(login), имя пользователя(name), дату рождения пользователя(birthday).

Таблица friends является связующей между несколькими пользователями, в ней хранится информация, какие пользователи являются друзьями.

Пример запроса:

```
SELECT *
FROM film
```
Пример запроса получения списка друзей: 

```
SELECT user.name
FROM user
INNER JOIN friends ON user.user_id = friends.friend_id
WHERE friends.user_id = 1;
```
