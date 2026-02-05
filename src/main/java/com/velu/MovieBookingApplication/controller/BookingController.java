package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.BookingDto;
import com.velu.MovieBookingApplication.dto.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

     @Autowired
     private BookingService bookingService;

//     we just create booking for show

     @PostMapping()
     public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto){
          return ResponseEntity.ok(bookingService.createBooking(bookingDto));
     }

     @PutMapping("/{id}")
     public ResponseEntity<BookingDto> updateBooking(@Valid @PathVariable Long id,@RequestBody BookingDto bookingDto){
            return ResponseEntity.ok(bookingService.updateBooking(id,bookingDto));
     }

     @GetMapping("/{id}/users")
    public ResponseEntity<PaginationResponse<BookingDto>> getUserBookings(
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
    public ResponseEntity<BookingDto> cancelBooking(@Valid @PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }


}
