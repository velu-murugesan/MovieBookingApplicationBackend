package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.TheaterDTO;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theater> addTheater(@RequestBody TheaterDTO theaterDTO){
         return ResponseEntity.ok(theaterService.addTheater(theaterDTO));
    }

    @PutMapping("/updatetheater/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id,@RequestBody TheaterDTO theaterDTO){
         return ResponseEntity.ok(theaterService.updateTheater(id,theaterDTO));
    }

    @GetMapping("/gettheaterbylocation")
    public ResponseEntity<List<Theater>> getTheaterByLocation(@RequestParam String location){
        return  ResponseEntity.ok(theaterService.getTheaterByLocation(location));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletetheater/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id){
         theaterService.deleteTheater(id);
         return ResponseEntity.ok().build();
    }

}
