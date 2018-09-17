package com.constantine.movierama.service;

import com.constantine.movierama.domain.Movie;
import com.constantine.movierama.utils.SortedBy;
import com.constantine.movierama.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestClientService {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${rest.api.host}")
    private String host;

    private static final String MOVIES_API = "/api/movies/";
    private static final String USERS_API = "/api/users/";
    private static final String MOVIES = "/movies/";

    public List<Movie> findAllMovies(SortedBy sortedBy) {
        return Arrays.stream(restTemplate.getForObject(host + MOVIES_API + "?sortedBy=" + sortedBy, Movie[].class)).collect(Collectors.toList());
    }

    public Movie findMovieById(int id) {
        return restTemplate.getForObject(host + MOVIES_API + id, Movie.class);
    }

    public List<Movie> findMoviesByUser(int id, SortedBy sortedBy) {
        return  Arrays.stream(restTemplate.getForObject(host + USERS_API + id + MOVIES + sortedBy.name(), Movie[].class)).collect(Collectors.toList());
    }

    public User findUserById(int id) {
        return restTemplate.getForObject(host + USERS_API + id, User.class);
    }

    public Movie create(int userId, Movie movie) {

        return restTemplate.postForObject(host + USERS_API + userId + MOVIES +"create", movie, Movie.class);
    }

    public User create(User user) {

        return restTemplate.postForObject(host + USERS_API + "create", user, User.class);
    }

    public User login(User user) {
        return restTemplate.postForObject(host + USERS_API + "login", user, User.class);
    }

    public Map ratingsForUserWithId(int id) {
        return restTemplate.getForObject(host + USERS_API + id + "/ratings", Map.class);
    }

    public User likeMovieForUser(int userId, int movieId) {
        return restTemplate.getForObject(host + USERS_API + userId + MOVIES + movieId + "/like", User.class);
    }

    public User hateMovieForUser(int userId, int movieId) {
        return restTemplate.getForObject(host + USERS_API + userId + MOVIES + movieId + "/hate", User.class);
    }

    public Long getLikesForMovie(int movieId) {
        return restTemplate.getForObject(host + MOVIES_API + movieId + "/likes", Long.class);
    }

    public Long getHatesForMovie(int movieId) {
        return restTemplate.getForObject(host + MOVIES_API + movieId + "/hates", Long.class);
    }
}
