package com.movieticket.controller;

import com.movieticket.model.Movie;
import com.movieticket.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/movie")
public class MovieConroller {
    @Autowired
    private MovieRepository movieRepository;
    @GetMapping
    public List<Movie> fetchMovies(){
        return movieRepository.findAll();

    }


}
