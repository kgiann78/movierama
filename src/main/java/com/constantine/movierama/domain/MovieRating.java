package com.constantine.movierama.domain;

import com.constantine.movierama.utils.Rating;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class MovieRating implements Serializable {

    @EmbeddedId
    private MovieRatingId movieRatingId;

    private Rating rating;

    public MovieRatingId getMovieRatingId() {
        return movieRatingId;
    }

    public void setMovieRatingId(MovieRatingId movieRatingId) {
        this.movieRatingId = movieRatingId;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
