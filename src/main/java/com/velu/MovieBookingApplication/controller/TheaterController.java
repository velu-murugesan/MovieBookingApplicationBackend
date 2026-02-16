package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.dto.TheaterDTO;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Negative;


@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theater> addTheater(@Valid @RequestBody TheaterDTO theaterDTO){
         return new ResponseEntity<Theater>(theaterService.addTheater(theaterDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theater> updateTheater(@Valid @PathVariable Long id,@RequestBody TheaterDTO theaterDTO){
         return ResponseEntity.ok(theaterService.updateTheater(id,theaterDTO));
    }

    @GetMapping()
    public ResponseEntity<PaginationResponse<Theater>> getTheaterByLocation(
            @Valid
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam String location
    ){
        return  ResponseEntity.ok(theaterService.getTheaterByLocation(page,size,location));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTheater(@Valid @PathVariable Long id){
         theaterService.deleteTheater(id);
    }

}
