package com.movieticket.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.movieticket.model.Movie;
import com.movieticket.model.MovieBookingDetail;
import com.movieticket.model.MovieInTheatre;
import com.movieticket.model.Seat;
import com.movieticket.model.SeatDetail;
import com.movieticket.model.Show;
import com.movieticket.model.Theatre;
import com.movieticket.model.User;
import com.movieticket.model.UserBooking;
import com.movieticket.repository.MovieInTheatreRespository;
import com.movieticket.repository.MovieRepository;
import com.movieticket.repository.TheatreRespository;
import com.movieticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/movie/admin")
public class TheatreAdminController {

    @Autowired
    private TheatreRespository theatreRespository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieInTheatreRespository movieInTheatreRespository;


    @PostMapping("/createTheatre")
    public void createTheatre(@RequestBody Theatre theatre) {
        theatreRespository.save(theatre);
    }

    @PostMapping("/addMovie")
    public void createMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
    }

    @PostMapping("/MovieInTheatre")
    public void movieINTheatre(@RequestBody MovieInTheatre movieInTheatreRequest) {

        Theatre theatre = theatreRespository.findById(movieInTheatreRequest.getTheatreId()).get();
        Show showDetails = theatre.getShows()
                .stream()
                .filter(show -> show.getTime().equals(movieInTheatreRequest.getTime()))
                .findFirst()
                .get();

        List<Seat> seats = showDetails.getSeats()
                .stream()
                .map(seatIdValueAtIndex -> {
                    String status = "Available";
                    if (seatIdValueAtIndex.getStatus() != null) {
                        status = seatIdValueAtIndex.getStatus();
                    }
                    Seat seatWithStatus = new Seat();
                    seatWithStatus.setId(seatIdValueAtIndex.getId());
                    seatWithStatus.setPrice(seatIdValueAtIndex.getPrice());
                    seatWithStatus.setStatus(status);
                    return seatWithStatus;
                })
                .collect(Collectors.toList());

        movieInTheatreRequest.setSeats(seats);
        movieInTheatreRespository.save(movieInTheatreRequest);


    }

    @GetMapping("/fetchMovieTheatres")
    public List<Theatre> fetchTheatresForMovie(@RequestParam(name = "movieId") Integer movieId) {
        List<MovieInTheatre> theatresRunningMovie = movieInTheatreRespository.findByMovieId(movieId);

        Set<Integer> uniqueTheatreIds = theatresRunningMovie.stream()
                .map(theatreRunningMovie -> theatreRunningMovie.getTheatreId())
                .collect(Collectors.toSet());

        List<Theatre> theatres = uniqueTheatreIds.stream()
                .map(theatreId -> theatreRespository.findById(theatreId).get())
                .collect(Collectors.toList());

        for (Theatre theatre : theatres) {

            List<Show> showsRunningTheMovie = theatresRunningMovie.stream()
                    .filter(x -> x.getTheatreId().equals(theatre.getId()))
                    .map(x -> {
                        Show show = new Show();
                        show.setTime(x.getTime());
                        return show;
                    })
                    .collect(Collectors.toList());

            theatre.setShows(showsRunningTheMovie);
        }
        return theatres;

    }

    @GetMapping("/fetchSeats")
    public List<Seat> fetchSeats(@RequestParam(name = "movieId") Integer movieId,
                                 @RequestParam(name = "theatreId") Integer theatreId,
                                 @RequestParam(name = "showTime") String time) {
        MovieInTheatre movieInTheatre = movieInTheatreRespository.findByMovieIdAndTheatreIdAndTime(movieId, theatreId, time).get();
        return movieInTheatre.getSeats();
    }

    @GetMapping("/fetchMoviesBooked")
    public List<UserBooking> fetchMoviesBooked(@RequestParam(name = "userId") Integer userId) {
        List<MovieInTheatre> movieInTheatres = movieInTheatreRespository.findAll();
        List<Movie> movies = movieRepository.findAll();
        List<Theatre> theatres = theatreRespository.findAll();

        return movies.stream()
                .map(movie -> movieInTheatres.stream()
                        .filter(movieInTheatre -> movieInTheatre.getMovieId().equals(movie.getId()))
                        .filter(movieInTheatre -> movieInTheatre.getSeats().stream().anyMatch(seat -> userId.equals(seat.getUserId())))
                        .map(movieInTheatre -> {
                            Theatre theatre = theatres.stream()
                                    .filter(x -> x.getId().equals(movieInTheatre.getTheatreId()))
                                    .findFirst().get();
                            String time = movieInTheatre.getTime();
                            List<Seat> seats = movieInTheatre.getSeats().stream().filter(seat -> userId.equals(seat.getUserId())).collect(Collectors.toList());

                            UserBooking userBooking = new UserBooking();
                            userBooking.setMovie(movie);
                            userBooking.setSeats(seats);
                            userBooking.setTheatre(theatre);
                            userBooking.setTime(time);
                            return userBooking;
                        })
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @GetMapping("/checkOverallBooking")
    public List<MovieBookingDetail> checkOverallBooking() {
        List<MovieInTheatre> movieInTheatres = movieInTheatreRespository.findAll();
        List<Movie> movies = movieRepository.findAll();
        List<Theatre> theatres = theatreRespository.findAll();
        List<User> users = userRepository.findAll();


        return movieInTheatres.stream()
                .map(movieInTheatre -> {
                    Movie movie = movies.stream()
                            .filter(a -> a.getId().equals(movieInTheatre.getMovieId()))
                            .findFirst().get();

                    Theatre theatre = theatres.stream()
                            .filter(a -> a.getId().equals(movieInTheatre.getTheatreId()))
                            .findFirst().get();
                    String time = movieInTheatre.getTime();

                    List<SeatDetail> collect = movieInTheatre.getSeats().stream()
                            .map(seat -> {
                                Integer userId = seat.getUserId();
                                Optional<User> user = users.stream().filter(u -> u.getUserId().equals(userId)).findFirst();
                                SeatDetail seatDetail = new SeatDetail();
                                seatDetail.setSeat(seat);
                                user.ifPresent(seatDetail::setUser);
                                return seatDetail;
                            })
                            .collect(Collectors.toList());

                    MovieBookingDetail movieBookingDetail = new MovieBookingDetail();
                    movieBookingDetail.setMovie(movie);
                    movieBookingDetail.setTheatre(theatre);
                    movieBookingDetail.setTime(time);
                    movieBookingDetail.setSeatDetails(collect);
                    return movieBookingDetail;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/fetchMovie")
    public Movie fetchSeats(@RequestParam(name = "movieId") Integer movieId) {
        Movie movie = movieRepository.findById(movieId).get();
        return movie;
    }

    @PostMapping("/changeSeatStatus")
    public void changeSeatStatus(@RequestParam(name = "movieId") Integer movieId,
                                 @RequestParam(name = "theatreId") Integer theatreId,
                                 @RequestParam(name = "showTime") String time,
                                 @RequestParam(name = "userId") Integer userId,
                                 @RequestParam(name = "seatIds") List<String> seatIds) {
        MovieInTheatre movieInTheatre = movieInTheatreRespository.findByMovieIdAndTheatreIdAndTime(movieId, theatreId, time).get();
        movieInTheatre.getSeats().stream()
                .filter(x -> seatIds.contains(x.getId()))
                .forEach(seat -> {
                    seat.setStatus("Booked");
                    seat.setUserId(userId);
                });
        movieInTheatreRespository.save(movieInTheatre);
    }

    public int sum(int number1, int number2) {
        int value = number1 + number2;
        return value;
    }
}
