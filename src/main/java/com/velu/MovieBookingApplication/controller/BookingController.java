package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.BookingDto;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

     @Autowired
     private BookingService bookingService;

//     we just create booking for show

     @PostMapping()
     public ResponseEntity<Booking> createBooking(@RequestBody BookingDto bookingDto){
          return ResponseEntity.ok(bookingService.createBooking(bookingDto));
     }

     @PutMapping("/{id}")
     public ResponseEntity<Booking> updateBooking(@PathVariable Long id,@RequestBody BookingDto bookingDto){
            return ResponseEntity.ok(bookingService.updateBooking(id,bookingDto));
     }

     @GetMapping("/users/{id}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long id){
           return ResponseEntity.ok(bookingService.getUserBookings(id));
     }


    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id){
         return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Booking>> getBookingbyUseridandStatus(@PathVariable Long id,@RequestParam BookingStatus bookingStatus){
         return ResponseEntity.ok(bookingService.getBookingbyUseridandStatus(id,bookingStatus));
    }

}
