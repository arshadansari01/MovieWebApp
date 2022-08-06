package com.movieticket.repository;

import com.movieticket.model.MovieInTheatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieInTheatreRespository extends JpaRepository <MovieInTheatre, Integer> {


   List<MovieInTheatre> findByMovieId(Integer movieId);
   Optional<MovieInTheatre> findByMovieIdAndTheatreIdAndTime(Integer movieId, Integer theatreId, String time);

}
