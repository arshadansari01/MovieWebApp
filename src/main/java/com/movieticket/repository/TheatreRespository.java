package com.movieticket.repository;

import com.movieticket.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheatreRespository extends JpaRepository <Theatre, Integer> {


    Optional<Theatre> findById(Integer theatreId);

}
