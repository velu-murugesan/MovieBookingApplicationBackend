package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.dto.PaginationResponse;
import com.velu.MovieBookingApplication.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
     public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieDTO movieDTO){
           return ResponseEntity.ok(movieService.addMovie(movieDTO));
     }

     @GetMapping()
    public ResponseEntity<PaginationResponse<MovieDTO>> getAllMovies(
            @Valid
           @RequestParam(defaultValue = "0") Integer page,
           @RequestParam(defaultValue = "10") Integer size,
           @RequestParam(required = false) String language,
           @RequestParam(required = false) String genre,
           @RequestParam(required = false) String title
     ){


         PageRequest pageRequest = PageRequest.of(page,size);

        return ResponseEntity.ok(movieService.getAllMovies(pageRequest,language,genre,title));

     }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovie(@Valid @PathVariable Long id,@RequestBody MovieDTO movieDTO){
         return ResponseEntity.ok(movieService.updateMovie(id,movieDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@Valid @PathVariable Long id){
      movieService.deleteMovie(id);
      return ResponseEntity.ok().build();
    }

}
