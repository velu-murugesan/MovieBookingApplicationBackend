package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.dto.ShowDTO;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

        Movie movie = movieRepository.findById(showDTO.getMovie_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No movie found for this id" + " " + showDTO.getMovie_id()));

        Theater theater = theaterRepository.findById(showDTO.getTheater_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No theater found for this id" + " " + showDTO.getTheater_id()));


        if(showRepository.existsByShowTimeAndMovieAndTheater(showDTO.getShowTime(),movie,theater)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"show is already exist");
        }

        Show show = new Show();
        show.setPrice(showDTO.getPrice());
        show.setShowTime(showDTO.getShowTime());
        show.setMovie(movie);
        show.setTheater(theater);

       return showRepository.save(show);
    }


    public Show updateShow(Long id, ShowDTO showDTO) {

       Show show =  showRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No movie found for this id" + " " + showDTO.getMovie_id()));

        Movie movie = movieRepository.findById(showDTO.getMovie_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No movie found for this id" + " " + showDTO.getMovie_id()));

        Theater theater = theaterRepository.findById(showDTO.getTheater_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No theater found for this id" + " " + showDTO.getTheater_id()));


        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);
        return showRepository.save(show);
    }

    public void deleteShow(Long id) {

        if(!showRepository.existsById(id)){
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No show available for the id" + " " + id);
        }

       List<Booking> bookings =  showRepository.findById(id).get().getBookings();

        if(!bookings.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Can't delete show with existing bookings");
        }
        else showRepository.deleteById(id);

    }

    public PaginationResponse<Show> getAllShows(PageRequest pageRequest, String movie, String theater) {

        Page<Show> shows;

        if(movie != null && theater != null && !movie.isEmpty() && !theater.isEmpty()){
            shows = showRepository.findShowsByMovieAndTheater(pageRequest,movie,theater);
        }
        else if(movie != null && !movie.isEmpty()){
          shows = showRepository.findShowsByMovie(pageRequest,movie);
        }else if(theater != null && !theater.isEmpty()){
          shows =  showRepository.findShowsByTheater(pageRequest,theater);
        }else{
            shows = showRepository.findAll(pageRequest);
        }
        return Utils.convertPageToPaginationResponse(shows);

    }
}
