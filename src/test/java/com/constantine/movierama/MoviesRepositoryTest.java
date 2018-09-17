package com.constantine.movierama;

import com.constantine.movierama.domain.Movie;
import com.constantine.movierama.domain.User;
import com.constantine.movierama.repositories.MovieRepository;
import com.constantine.movierama.repositories.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;

public class MoviesRepositoryTest extends BaseRepositoryTest {

    private static Logger logger = LoggerFactory.getLogger(MoviesRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void moviesCRUDTest() {

            User user = new User();
            user.setName("test_user");
            user.setSurname("surname");
            user.setUsername("username1");
            user.setPassword("password");


            logger.info("Inserting new user with name {}", user.getName());
            user = userRepository.save(user);
            assertNotEquals(0, user.getId());

            logger.info("New user has id {}", user.getId());

            Movie movie = new Movie();
            movie.setTitle("A title");
            movie.setDescription("A description");
            movie.setDateOfPublication(LocalDateTime.now(ZoneId.of("UTC")));
            movie.setUser(user);

            logger.info("Inserting new movie with title {}", movie.getTitle());
            movie = movieRepository.save(movie);

            assertNotNull(movie);
            assertNotNull(movie.getUser());
            assertNotEquals(0, movie.getId());
            assertEquals(user.getId(), movie.getUser().getId());

            logger.info("New movie has id {} for user with id {}", movie.getId(), user.getId());

            int userId = movie.getUser().getId();
            int movieId = movie.getId();

            logger.info("Found user with id {}", userId);

            movieRepository.delete(movie);


            movie = movieRepository.findById(movieId).orElse(null);
            assertNull(movie);
            logger.info("Deleting movie with id {}", movieId);

            user = userRepository.findById(userId).orElseGet(null);

            assertNotNull(user);
            logger.info("Asserting that we didn't delete user");
    }
}
