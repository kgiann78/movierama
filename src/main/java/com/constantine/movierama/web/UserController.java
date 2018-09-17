package com.constantine.movierama.web;

import com.constantine.movierama.domain.Movie;
import com.constantine.movierama.domain.MovieRating;
import com.constantine.movierama.domain.MovieRatingId;
import com.constantine.movierama.domain.User;
import com.constantine.movierama.repositories.MovieRepository;
import com.constantine.movierama.repositories.UserRepository;
import com.constantine.movierama.utils.Rating;
import com.constantine.movierama.utils.SortedBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<User> findAll() {

        return userRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    User findById(@PathVariable("id") int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @RequestMapping(value = "/{id}/movies/{sorted}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<Movie> findMoviesByUser(@PathVariable("id") int userId, @PathVariable("sorted") String sorted) {

        SortedBy sortedBy = SortedBy.valueOf(sorted);

        switch (sortedBy) {
            case LIKES:
                return movieRepository.findAllByUserIdOrderByLikesDesc(userId);
            case HATES:
                return movieRepository.findAllByUserIdOrderByHatesDesc(userId);
                default:
                    return movieRepository.findAllByUserIdOrderByDateOfPublicationDesc(userId);
        }
    }

    @RequestMapping(value = "/{id}/movies/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Movie createMovie(@PathVariable("id") int userId, @RequestBody Movie movie) {

        User user;
        if ((user = userRepository.findById(userId).orElse(null)) != null) {

            movie.setUser(user);
            createMovieRating(movie, user, Rating.OWNER);

            return movieRepository.save(movie);
        }

        return null;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    User create(@RequestBody User user) {

        return userRepository.save(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    User login(@RequestBody User user) {
        user = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        return user;
    }

    @RequestMapping(value = "/{id}/ratings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<Integer, Rating> ratings(@PathVariable("id") int userId) {
        User user;

        if ((user = userRepository.findById(userId).orElse(null)) != null) {
            return user.getMovieRatings()
                    .stream().collect(Collectors.toMap(movierating -> movierating.getMovieRatingId().getMovie().getId(), MovieRating::getRating));
        }
        return null;
    }

    @RequestMapping(value = "/{user_id}/movies/{movie_id}/like", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    User like(@PathVariable("user_id") int userId, @PathVariable("movie_id") int movieId) {
            return rateMovie(userId, movieId, Rating.LIKE);
    }


    @RequestMapping(value = "/{user_id}/movies/{movie_id}/hate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    User hate(@PathVariable("user_id") int userId, @PathVariable("movie_id") int movieId) {
        return rateMovie(userId, movieId, Rating.HATE);
    }

    private User rateMovie(int userId, int movieId, Rating rating) {
        Movie movie;
        User user;

        if ((movie = movieRepository.findById(movieId).orElse(null)) != null &&
                (user = userRepository.findById(userId).orElse(null)) != null) {
            Optional<MovieRating> optionalMovieRating = movie.getMovieRatings()
                    .stream()
                    .filter(movieRating1 -> movieRating1.getMovieRatingId().getUser().getId() == userId).findFirst();

            MovieRating movieRating;

            if (optionalMovieRating.isPresent()) {

                movieRating = optionalMovieRating.get();

                if (movieRating.getRating() == Rating.NOTHING) {

                    movieRating.setRating(rating);
                    movie.getMovieRatings().add(movieRating);
                } else {
                    toggleLikesAndHates(movieRating, rating);
                }
            } else {
                createMovieRating(movie, user, rating);
            }

            movie.setLikes(movie.getMovieRatings().stream().filter(movieRating1 -> movieRating1.getRating() == Rating.LIKE).count());
            movie.setHates(movie.getMovieRatings().stream().filter(movieRating1 -> movieRating1.getRating() == Rating.HATE).count());

            movieRepository.save(movie);

            return userRepository.findById(user.getId()).orElse(null);
        }
        return null;
    }

    private void createMovieRating(Movie movie, User user, Rating rating) {

        MovieRating movieRating = new MovieRating();
        MovieRatingId movieRatingId = new MovieRatingId();
        movieRatingId.setUser(user);
        movieRatingId.setMovie(movie);

        movieRating.setMovieRatingId(movieRatingId);
        movieRating.setRating(rating);
        movie.getMovieRatings().add(movieRating);
    }


    private void toggleLikesAndHates(MovieRating movieRating, Rating rating) {
        if (movieRating.getRating() == rating) {
            movieRating.setRating(Rating.NOTHING);
        } else {
            movieRating.setRating(rating);
        }
    }


}
