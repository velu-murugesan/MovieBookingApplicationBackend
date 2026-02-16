package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieDTO movieDTO){

           return new ResponseEntity<Movie>(movieService.addMovie(movieDTO), HttpStatus.CREATED);
     }

     @GetMapping()
    public ResponseEntity<PaginationResponse<Movie>> getAllMovies(
            @Valid
           @RequestParam(defaultValue = "0") Integer page,
           @RequestParam(defaultValue = "10") Integer size,
           @RequestParam(required = false) String language,
           @RequestParam(required = false) String genre,
           @RequestParam(required = false) String title
     ){

         int maxPageSize = 20;
         int pageSize =  Math.min(maxPageSize,Math.abs(size));
         PageRequest pageRequest = PageRequest.of(page,pageSize);

        return ResponseEntity.ok(movieService.getAllMovies(pageRequest,language,genre,title));

     }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> updateMovie(@Valid @PathVariable Long id,@RequestBody MovieDTO movieDTO){
         return ResponseEntity.ok(movieService.updateMovie(id,movieDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMovie(@Valid @PathVariable Long id){
      movieService.deleteMovie(id);
    }

}
