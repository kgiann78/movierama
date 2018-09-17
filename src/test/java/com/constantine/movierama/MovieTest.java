package com.constantine.movierama;

import com.constantine.movierama.domain.Movie;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovieTest {

    @Test
    void movieHasTitleTest() {
        Movie movie = new Movie();
        movie.setTitle("Movie Title");

        assertEquals("Movie Title", movie.getTitle());
    }

    @Test
    void movieHasDescriptionTest() {
        Movie movie = new Movie();
        movie.setDescription("This is a description");

        assertEquals("This is a description", movie.getDescription());
    }

}
