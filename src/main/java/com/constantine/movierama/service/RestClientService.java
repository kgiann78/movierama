package com.constantine.movierama.service;

import com.constantine.movierama.domain.Movie;
import com.constantine.movierama.utils.Rating;
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


    public List<Movie> findAllMovies(SortedBy sortedBy) {
        return Arrays.stream(restTemplate.getForObject(host + "/api/movies?sortedBy=" + sortedBy, Movie[].class)).collect(Collectors.toList());
    }

    public Movie findMovieById(int id) {
        return restTemplate.getForObject(host + "/api/movies/" + id, Movie.class);
    }

    public List<Movie> findMoviesByUser(int id, SortedBy sortedBy) {
        return  Arrays.stream(restTemplate.getForObject(host + "/api/users/" + id + "/movies/" + sortedBy.name(), Movie[].class)).collect(Collectors.toList());
    }

    public User findUserById(int id) {
        return restTemplate.getForObject(host + "/api/users/" + id, User.class);
    }

    public Movie create(int userId, Movie movie) {

        return restTemplate.postForObject(host + "/api/users/" + userId + "/movies/create", movie, Movie.class);
    }

    public User create(User user) {

        return restTemplate.postForObject(host + "/api/users/create", user, User.class);
    }

    public User login(User user) {
        return restTemplate.postForObject(host + "/api/users/login", user, User.class);
    }

    public Map<Integer, Rating> ratingsForUserWithId(int id) {
        return restTemplate.getForObject(host + "/api/users/" + id + "/ratings", Map.class);
    }

    public User likeMovieForUser(int userId, int movieId) {
        return restTemplate.getForObject(host + "/api/users/" + userId + "/movies/" + movieId + "/like", User.class);
    }

    public User hateMovieForUser(int userId, int movieId) {
        return restTemplate.getForObject(host + "/api/users/" + userId + "/movies/" + movieId + "/hate", User.class);
    }

    public Long getLikesForMovie(int movieId) {
        return restTemplate.getForObject(host + "/api/movies/" + movieId + "/likes", Long.class);
    }

    public Long getHatesForMovie(int movieId) {
        return restTemplate.getForObject(host + "/api/movies/" + movieId + "/hates", Long.class);
    }
}
