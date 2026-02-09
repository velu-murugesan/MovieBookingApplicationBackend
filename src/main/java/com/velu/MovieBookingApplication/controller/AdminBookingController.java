package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginationResponse<Booking>> getAllBookings(
            @Valid
           @RequestParam(defaultValue = "0") Integer page,
           @RequestParam(defaultValue = "10") Integer size,
           @RequestParam(required = false) BookingStatus status
    ){
         return ResponseEntity.ok(bookingService.getAllBookings(page,size,status));
    }

    @GetMapping("/{id}/shows")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginationResponse<Booking>> getShowBookings(
            @Valid
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
            ){
        return ResponseEntity.ok(bookingService.getShowBookings(page,size,id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBooking(@Valid @PathVariable Long id){
        bookingService.deleteBooking(id);
    }

}
