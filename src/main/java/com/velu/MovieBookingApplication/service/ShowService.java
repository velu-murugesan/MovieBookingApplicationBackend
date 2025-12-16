package com.velu.MovieBookingApplication.service;


import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dto.ShowDTO;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public Show createShow(ShowDTO showDTO) {

        Movie movie = movieRepository.findById(showDTO.getMovie_id()).orElseThrow(() -> new CustomException("No movie found for this id" + " " + showDTO.getMovie_id()));

        Theater theater = theaterRepository.findById(showDTO.getTheater_id()).orElseThrow(() -> new CustomException("No theater found for this id" + " " + showDTO.getTheater_id()));

        Show show = new Show();
        show.setPrice(showDTO.getPrice());
        show.setShowTime(showDTO.getShowTime());
        show.setMovie(movie);
        show.setTheater(theater);

       return showRepository.save(show);
    }

    public List<Show> getAllShows() {
       return  showRepository.findAll();
    }

    public List<Show> getAllShowsByMovie(Long movieId) {
        Optional<List<Show>> listOfShowsBox =  showRepository.findByMovieId(movieId);

        if(listOfShowsBox.isPresent()){
            return listOfShowsBox.get();
        }

        else throw new CustomException("No more shows found for the movie id" + " " + movieId);
    }

    public List<Show> getAllShowsByTheater(Long theaterId) {
        Optional<List<Show>> listOfShowsBox =  showRepository.findByTheaterId(theaterId);

        if(listOfShowsBox.isPresent()){
            return listOfShowsBox.get();
        }
        else throw new CustomException("NO more shows found for the theater id" + " " + theaterId);
    }

    public Show updateShow(Long id, ShowDTO showDTO) {

       Show show =  showRepository.findById(id).orElseThrow(() -> new CustomException("No Movie found for this id" + " " + id));

        Movie movie = movieRepository.findById(showDTO.getMovie_id()).orElseThrow(() -> new CustomException("No movie found for this id" + " " + showDTO.getMovie_id()));

        Theater theater = theaterRepository.findById(showDTO.getTheater_id()).orElseThrow(() -> new CustomException("No theater found for this id" + " " + showDTO.getTheater_id()));


        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);
        return showRepository.save(show);
    }

    public void deleteShow(Long id) {

        if(!showRepository.existsById(id)){
             throw new CustomException("No show available for the id" + " " + id);
        }

       List<Booking> bookings =  showRepository.findById(id).get().getBookings();


        if(!bookings.isEmpty()){
            throw new CustomException("Can't delete show with existing bookings");
        }
        else showRepository.deleteById(id);


    }
}
