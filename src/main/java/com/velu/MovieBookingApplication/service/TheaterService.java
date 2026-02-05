package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dto.PaginationResponse;
import com.velu.MovieBookingApplication.dto.TheaterDTO;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.exception.BadRequestException;
import com.velu.MovieBookingApplication.exception.ResourceNotFoundException;
import com.velu.MovieBookingApplication.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



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
        Theater theater =  theaterRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("No theater available for the id" + " " + id));

        theater.setTheaterLocation(theaterDTO.getTheaterLocation());
        theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
        theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
        theater.setTheaterName(theaterDTO.getTheaterName());

        return theaterRepository.save(theater);
    }

    public PaginationResponse<TheaterDTO> getTheaterByLocation(Integer page, Integer size, String location) {

        Sort sort = Sort.by(Sort.Order.asc("theaterName"));

        if(location == null || location.isBlank()){
             throw new BadRequestException("Location is empty , please give the location");
        }

        if(!theaterRepository.existsByLocation(location)){
          throw new ResourceNotFoundException("location is not exist" + location);
        }

        PageRequest pageRequest = PageRequest.of(page,size,sort);

       Page<Theater> theaters = theaterRepository.findByTheaterLocation(pageRequest,location);

       return Utils.convertPageToPaginationResponse(theaters,Utils::convertTheaterToTheaterDto);
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }
}
