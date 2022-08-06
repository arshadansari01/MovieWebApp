package com.movieticket.model;

import java.util.List;

public class MovieBookingDetail {

    private Movie movie;

    private Theatre theatre;

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private List<SeatDetail> seatDetails;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public List<SeatDetail> getSeatDetails() {
        return seatDetails;
    }

    public void setSeatDetails(List<SeatDetail> seatDetails) {
        this.seatDetails = seatDetails;
    }
}
