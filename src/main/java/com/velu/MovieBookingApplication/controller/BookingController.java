package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.BookingDTO;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

     @Autowired
     private BookingService bookingService;

//     we just create booking for show

     @PostMapping()
     public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingDTO bookingDto){
          return new ResponseEntity<Booking>(bookingService.createBooking(bookingDto), HttpStatus.CREATED);
     }

     @PutMapping("/{id}")
     public ResponseEntity<Booking> updateBooking(@Valid @PathVariable Long id, @RequestBody BookingDTO bookingDto){
            return ResponseEntity.ok(bookingService.updateBooking(id,bookingDto));
     }

     @GetMapping("/{id}/users")
    public ResponseEntity<PaginationResponse<Booking>> getUserBookings(
            @Valid
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) BookingStatus bookingStatus
     ){

         if(bookingStatus != null){
             return ResponseEntity.ok(bookingService.getBookingbyUseridandStatus(page,size,id,bookingStatus));
         }
           return ResponseEntity.ok(bookingService.getUserBookings(page,size,id));
     }


    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@Valid @PathVariable Long id){
         return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@Valid @PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }


}
