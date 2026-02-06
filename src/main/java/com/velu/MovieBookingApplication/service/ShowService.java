package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dto.PaginationResponse;
import com.velu.MovieBookingApplication.dto.ShowDTO;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.exception.DeleteShowConflictException;
import com.velu.MovieBookingApplication.exception.ResourceNotFoundException;
import com.velu.MovieBookingApplication.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public Show createShow(ShowDTO showDTO) {

        Movie movie = movieRepository.findById(showDTO.getMovie_id()).orElseThrow(() -> new ResourceNotFoundException("No movie found for this id" + " " + showDTO.getMovie_id()));

        Theater theater = theaterRepository.findById(showDTO.getTheater_id()).orElseThrow(() -> new ResourceNotFoundException("No theater found for this id" + " " + showDTO.getTheater_id()));

        Show show = new Show();
        show.setPrice(showDTO.getPrice());
        show.setShowTime(showDTO.getShowTime());
        show.setMovie(movie);
        show.setTheater(theater);

       return showRepository.save(show);
    }


    public Show updateShow(Long id, ShowDTO showDTO) {

       Show show =  showRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Movie found for this id" + " " + id));

        Movie movie = movieRepository.findById(showDTO.getMovie_id()).orElseThrow(() -> new ResourceNotFoundException("No movie found for this id" + " " + showDTO.getMovie_id()));

        Theater theater = theaterRepository.findById(showDTO.getTheater_id()).orElseThrow(() -> new ResourceNotFoundException("No theater found for this id" + " " + showDTO.getTheater_id()));


        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);
        return showRepository.save(show);
    }

    public void deleteShow(Long id) {

        if(!showRepository.existsById(id)){
             throw new ResourceNotFoundException("No show available for the id" + " " + id);
        }

       List<Booking> bookings =  showRepository.findById(id).get().getBookings();


        if(!bookings.isEmpty()){
            throw new DeleteShowConflictException("Can't delete show with existing bookings");
        }
        else showRepository.deleteById(id);


    }

    public PaginationResponse<ShowDTO> getAllShows(PageRequest pageRequest, String movie, String theater) {

        Page<Show> shows;

        if(movie != null){
          shows = showRepository.findAll(movie,pageRequest);
        }else if(theater != null){
          shows =  showRepository.findAll(pageRequest,theater);
        }

       shows = showRepository.findAll(pageRequest);

        return Utils.convertPageToPaginationResponse(shows,Utils::convertShowToShowDTO);

    }
}
