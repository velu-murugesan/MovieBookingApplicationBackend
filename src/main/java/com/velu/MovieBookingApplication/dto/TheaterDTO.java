package com.velu.MovieBookingApplication.dto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TheaterDTO {
    @NotBlank(message = "Theater name should not be blank")
    private String theaterName;
    @NotBlank(message = "Theater location should not be blank")
    private String theaterLocation;
    @NotBlank(message = "Theater location should not be blank")
    private String theaterScreenType;
    @NotNull(message = "Theater Capacity is required")
    @Min(value = 500,message = "Theater Capacity Minimum 500")
    @Max(value = 5000,message = "Theater Capacity Maximum 5000")
    private Integer theaterCapacity;
}
