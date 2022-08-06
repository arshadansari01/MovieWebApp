package com.movieticket.controller;
import com.movieticket.model.MovieInTheatre;
import com.movieticket.model.Seat;
import com.movieticket.repository.MovieInTheatreRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TheatreAdminControllerTest {
    @InjectMocks
    TheatreAdminController theatreAdminController;
    @Mock
    MovieInTheatreRespository movieInTheatreRespository;

    @Test
    void fetchSeatsShouldReturnSeatsOfParticularMovieInAParticularTheatreAtAGivenTime() {
        Integer movieId = 2;
        Integer theatreId = 4;
        String movieTime = "12:45 PM";
        MovieInTheatre movieInTheatre = new MovieInTheatre();
        Seat seat = new Seat();
        seat.setId("A1");
        seat.setPrice(250);
        seat.setStatus("Available");

        List<Seat> seats = new ArrayList<>();
        seats.add(seat);
        movieInTheatre.setSeats(seats);
        Mockito.lenient().when(movieInTheatreRespository.findByMovieIdAndTheatreIdAndTime(2, theatreId, movieTime))
                .thenReturn(Optional.of(movieInTheatre));
        List<Seat> actual = theatreAdminController.fetchSeats(movieId, theatreId, movieTime);
        assertEquals(seats, actual);
    }

}
    @Test
    void sumShouldReturnSumOfTwoInteger() {
        TheatreAdminController theatreAdminController = new TheatreAdminController();
        int actual = theatreAdminController.sum(25, 77);
        assertEquals(102, actual);


    }


}