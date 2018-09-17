package com.constantine.movierama.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie {

    private int id;
    private String title;
    private String description;
    private User user;
    private LocalDateTime dateOfPublication;
    private long hates;
    private long likes;

    private Set<MovieRating> movieRatings;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(LocalDateTime dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public long getHates() {
        return hates;
    }

    public void setHates(long hates) {
        this.hates = hates;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    @OneToMany(mappedBy = "movieRatingId.movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public Set<MovieRating> getMovieRatings() {
        if (movieRatings == null) movieRatings = new HashSet<>();
        return movieRatings;
    }

    public void setMovieRatings(Set<MovieRating> movieRatings) {
        this.movieRatings = movieRatings;
    }

    @PrePersist
    void createdAt() {
        this.dateOfPublication = LocalDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", dateOfPublication=" + dateOfPublication +
                '}';
    }
}
