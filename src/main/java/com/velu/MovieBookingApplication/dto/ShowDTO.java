package com.velu.MovieBookingApplication.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
public class ShowDTO {

    @NotNull(message = "showTime is required")
    @FutureOrPresent(message = "show time should not be in past")
    private LocalDateTime showTime;
    @NotNull(message = "price is required")
    @Positive(message = "price must be greater then 0")
    private Double price;
    @NotNull(message = "movie_id is required")
    private Long movie_id;
    @NotNull(message = "theater_id is required")
    private Long theater_id;

}
