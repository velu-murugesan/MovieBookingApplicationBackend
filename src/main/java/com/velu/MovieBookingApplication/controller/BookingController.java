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
@RequestMapping("/api/booking")
public class BookingController {

     @Autowired
     private BookingService bookingService;

//     we just create booking for show

     @PostMapping("/createbooking")
     public ResponseEntity<Booking> createBooking(@RequestBody BookingDto bookingDto){
          return ResponseEntity.ok(bookingService.createBooking(bookingDto));
     }

     @PutMapping("/updatebooking/{id}")
     public ResponseEntity<Booking> updateBooking(@PathVariable Long id,@RequestBody BookingDto bookingDto){
            return ResponseEntity.ok(bookingService.updateBooking(id,bookingDto));
     }

     @GetMapping("/getuserbookings/{id}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long id){
           return ResponseEntity.ok(bookingService.getUserBookings(id));
     }


    @GetMapping("/getshowbookings/{id}")
    public ResponseEntity<List<Booking>> getShowBookings(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getShowBookings(id));
    }

//    we just update update the booking status

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id){
         return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/getbookingsbystatus/{bookingstatus}")
    public ResponseEntity<List<Booking>> getBookingbyStatus(@PathVariable BookingStatus bookingStatus){
         return ResponseEntity.ok(bookingService.getBookingbyStatus(bookingStatus));
    }

    @DeleteMapping("/deletebooking/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id){
         bookingService.deleteBooking(id);
         return ResponseEntity.ok().build();
    }
}
