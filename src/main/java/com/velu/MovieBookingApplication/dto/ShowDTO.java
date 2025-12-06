package com.velu.MovieBookingApplication.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowDTO {

    private LocalDateTime showTime;
    private Double price;
    private Long movie_id;
    private Long theater_id;

}
