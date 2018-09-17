package com.constantine.movierama.web;

import com.constantine.movierama.domain.Movie;
import com.constantine.movierama.utils.Rating;
import com.constantine.movierama.utils.SortedBy;
import com.constantine.movierama.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<Movie> findAllOrderedBy(@RequestParam("sortedBy")SortedBy sortedBy) {

        switch (sortedBy) {
            case LIKES:
                return movieRepository.findAllByOrderByLikesDesc();
            case HATES:
                return movieRepository.findAllByOrderByHatesDesc();
            default:
                return movieRepository.findAllByOrderByDateOfPublicationDesc();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Movie findById(@PathVariable("id") int id) {

        return movieRepository.findById(id).orElse(null);
    }

    @RequestMapping(value = "/{id}/likes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    long getLikesForMovie(@PathVariable("id") int id) {
        Movie movie;

        if ((movie = movieRepository.findById(id).orElse(null)) != null) {
            return movie.getMovieRatings().stream().filter(movieRating1 -> movieRating1.getRating() == Rating.LIKE).count();
        }
        return 0L;
    }

    @RequestMapping(value = "/{id}/hates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    long getHatesForMovie(@PathVariable("id") int id) {
        Movie movie;

        if ((movie = movieRepository.findById(id).orElse(null)) != null) {
            return movie.getMovieRatings().stream().filter(movieRating1 -> movieRating1.getRating() == Rating.HATE).count();
        }
        return 0L;
    }
}
