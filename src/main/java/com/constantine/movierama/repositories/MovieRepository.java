package com.constantine.movierama.repositories;

import com.constantine.movierama.domain.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

    @Query("select m from Movie m where m.user.id = :userId order by dateOfPublication desc")
    Iterable<Movie> findAllByUserIdOrderByDateOfPublicationDesc(@Param("userId") int userId);

    Iterable<Movie> findAllByUserIdOrderByLikesDesc(int userId);

    Iterable<Movie> findAllByUserIdOrderByHatesDesc(int userId);

    Iterable<Movie> findAllByOrderByDateOfPublicationDesc();

    Iterable<Movie> findAllByOrderByLikesDesc();

    Iterable<Movie> findAllByOrderByHatesDesc();
}
