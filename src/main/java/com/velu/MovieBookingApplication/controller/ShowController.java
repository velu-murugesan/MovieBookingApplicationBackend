package com.velu.MovieBookingApplication.controller;


import com.velu.MovieBookingApplication.dto.ShowDTO;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping("/createshow")
    public ResponseEntity<Show> createShow(@RequestBody ShowDTO showDTO){
          return ResponseEntity.ok(showService.createShow(showDTO));
    }

    @GetMapping("/getallshows")
    public ResponseEntity<List<Show>> getAllShows(){
       return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/getshowsbymovie/{id}")
    public ResponseEntity<List<Show>> getShowsByMovie(@PathVariable Long movieId){
       return  ResponseEntity.ok( showService.getAllShowsByMovie(movieId));
    }

    @GetMapping("/getshowsbytheater/{id}")
    public ResponseEntity<List<Show>> getShowsByTheater(@PathVariable Long theaterId){
        return ResponseEntity.ok(showService.getAllShowsByTheater(theaterId));
    }

    @PutMapping("/updateshow/{id}")
    public ResponseEntity<Show> updateShow(@PathVariable Long id,@RequestBody ShowDTO showDTO){
        return  ResponseEntity.ok(showService.updateShow(id,showDTO));
    }

    @DeleteMapping("/deleteshow/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id){
        showService.deleteShow(id);
        return ResponseEntity.ok().build();
    }

}
