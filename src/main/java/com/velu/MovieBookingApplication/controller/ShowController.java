package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.dto.ShowDTO;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/shows")
public class ShowController {


    @Autowired
    private ShowService showService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> createShow(@Valid @RequestBody ShowDTO showDTO){
          return new ResponseEntity<Show>(showService.createShow(showDTO), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<PaginationResponse<Show>> getAllShows(
            @Valid
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String movie,
            @RequestParam(required = false) String theater
    ){
        PageRequest pageRequest = PageRequest.of(page,size);

       return ResponseEntity.ok(showService.getAllShows(pageRequest,movie,theater));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> updateShow(@Valid @PathVariable Long id,@RequestBody ShowDTO showDTO){
        return  ResponseEntity.ok(showService.updateShow(id,showDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteShow(@Valid @PathVariable Long id){
        showService.deleteShow(id);
    }

}
