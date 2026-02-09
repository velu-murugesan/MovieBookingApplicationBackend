package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.dto.TheaterDTO;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TheaterService {

    @Autowired TheaterRepository theaterRepository;


    public Theater addTheater(TheaterDTO theaterDTO) {

        Theater theater = new Theater();
        theater.setTheaterName(theaterDTO.getTheaterName());
        theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
        theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
        theater.setTheaterLocation(theaterDTO.getTheaterLocation());

        return theaterRepository.save(theater);
    }

    public Theater updateTheater(Long id, TheaterDTO theaterDTO) {
        Theater theater =  theaterRepository.findById(id).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,"No theater available for the id" + " " + id));

        theater.setTheaterLocation(theaterDTO.getTheaterLocation());
        theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
        theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
        theater.setTheaterName(theaterDTO.getTheaterName());

        return theaterRepository.save(theater);
    }

    public PaginationResponse<Theater> getTheaterByLocation(Integer page, Integer size, String location) {

        Sort sort = Sort.by(Sort.Order.asc("theaterName"));

        if(location == null || location.isBlank()){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Location is empty , please give the location");
        }

        if(!theaterRepository.existsByTheaterLocation(location)){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,"location is not exist" + location);
        }

        PageRequest pageRequest = PageRequest.of(page,size,sort);

       Page<Theater> theaters = theaterRepository.findByTheaterLocation(pageRequest,location);

       return Utils.convertPageToPaginationResponse(theaters);
    }

    public void deleteTheater(Long id) {

        if(!theaterRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"theater id is not exist");
        }

        theaterRepository.deleteById(id);
    }
}
