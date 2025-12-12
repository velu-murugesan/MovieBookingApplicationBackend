package com.velu.MovieBookingApplication.dto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDto {
    private Integer numberOfSeats;
    private LocalDateTime bookingDate;
    private List<String> seatNumbers;
    private Long userId;
    private Long showId;
}
