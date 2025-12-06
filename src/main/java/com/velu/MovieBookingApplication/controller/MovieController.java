package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<Movie> addMovie(@RequestBody MovieDTO movieDTO){
           return ResponseEntity.ok(movieService.addMovie(movieDTO));
     }

     @GetMapping("/getallmovies")
    public ResponseEntity<List<Movie>> getAllMovies(){
          return ResponseEntity.ok(movieService.getAllMovies());
     }


     @GetMapping("/getallmoviesbygenre")
     public ResponseEntity<List<Movie>> getAllMoviesByGenre(@RequestParam String genre){
           return ResponseEntity.ok(movieService.getAllMoviesByGenre(genre));
     }

    @GetMapping("/getallmoviesbylanguage")
    public ResponseEntity<List<Movie>> getAllMoviesByLanguage(@RequestParam String language){
        return ResponseEntity.ok(movieService.getAllMoviesByLanguage(language));
    }

    @GetMapping("/getmoviebytitle")
    public ResponseEntity<Movie> getMovieByTitle(@RequestParam String title){
        return ResponseEntity.ok(movieService.getMovieByTitle(title));
    }

    @PutMapping("/updatemovie/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id,@RequestBody MovieDTO movieDTO){
         return ResponseEntity.ok(movieService.updateMovie(id,movieDTO));
    }

    @DeleteMapping("/deletemovie/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
      movieService.deleteMovie(id);
      return ResponseEntity.ok().build();
    }

}
