package com.constantine.movierama.repositories;

import com.constantine.movierama.domain.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

    Iterable<Movie> findAllByUserIdOrderByDateOfPublicationDesc(int userId);

    Iterable<Movie> findAllByUserIdOrderByLikesDesc(int userId);

    Iterable<Movie> findAllByUserIdOrderByHatesDesc(int userId);

    Iterable<Movie> findAllByOrderByDateOfPublicationDesc();

    Iterable<Movie> findAllByOrderByLikesDesc();

    Iterable<Movie> findAllByOrderByHatesDesc();
}
