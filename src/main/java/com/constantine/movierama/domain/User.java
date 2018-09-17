package com.constantine.movierama.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints={@UniqueConstraint(columnNames = "username")})
public class User {

    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private List<Movie> movies;

    private Set<MovieRating> movieRatings;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Movie> getMovies() {
        if (movies == null) movies = new ArrayList<>();

        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @OneToMany(mappedBy = "movieRatingId.user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public Set<MovieRating> getMovieRatings() {
        if (movieRatings == null) movieRatings = new HashSet<>();
        return movieRatings;
    }

    public void setMovieRatings(Set<MovieRating> movieRatings) {
        this.movieRatings = movieRatings;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
