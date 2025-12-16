package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dto.TheaterDTO;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Theater theater =  theaterRepository.findById(id).orElseThrow(() ->  new CustomException("No theater available for the id" + " " + id));

        theater.setTheaterLocation(theaterDTO.getTheaterLocation());
        theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
        theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
        theater.setTheaterName(theaterDTO.getTheaterName());

        return theaterRepository.save(theater);
    }

    public List<Theater> getTheaterByLocation(String location) {
         Optional<List<Theater>> listOfTheaterBox =  theaterRepository.findByTheaterLocation(location);

         if(listOfTheaterBox.isPresent()){
             return listOfTheaterBox.get();
         }
         else throw new CustomException("Theater is not found for this location" + " " + location);
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }
}
