package com.velu.MovieBookingApplication.dto;


import lombok.Data;

@Data
public class TheaterDTO {
    private String theaterName;
    private String theaterLocation;
    private String theaterScreenType;
    private Integer theaterCapacity;
}
