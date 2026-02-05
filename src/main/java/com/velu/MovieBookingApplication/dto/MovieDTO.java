package com.velu.MovieBookingApplication.dto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class MovieDTO {
    @NotBlank(message = "movie name should be present and without trailing space's")
    private String name;
    @NotNull(message = "description is required")
    private String description;
    @NotNull(message = "genre is required")
    private String genre;
    @NotNull(message = "language is required")
    private String language;
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 hour")
    @Max(value = 500, message = "Duration cannot exceed 500 minutes")
    private Integer duration;
    @NotNull(message = "releaseDate is required")
    @PastOrPresent(message = "movie releaseDate should not be in future")
    private LocalDate release_date;
}
