package com.velu.MovieBookingApplication.dto;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingDto {

    @Min(value = 1,message = "At least 1 seat must be selected")
    private Integer numberOfSeats;
    @NotNull(message = "Booking Time is required")
    private LocalDateTime bookingDate;
    @Min(value = 1,message = "At least 1 seat must be booked")
    private List<String> seatNumbers;
    @NotNull(message = "UserId is required")
    private Long userId;
    @NotNull(message = "ShowId is required")
    private Long showId;
}



//  numberOfSeats -> how many seats that i want
// seatNumbers -> which seats that i actually want