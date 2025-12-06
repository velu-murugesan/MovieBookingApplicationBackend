package com.velu.MovieBookingApplication.dto;

import com.velu.MovieBookingApplication.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDto {
    private Integer numberOfSeats;
    private LocalDateTime bookingDate;
    private Double price;
    private BookingStatus bookingStatus;
    private List<String> seatNumbers;
    private Long userId;
    private Long showId;
}
