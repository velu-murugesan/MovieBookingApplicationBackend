package com.velu.MovieBookingApplication.Utils;

import com.velu.MovieBookingApplication.dto.*;
import com.velu.MovieBookingApplication.entity.*;
import org.springframework.data.domain.Page;

public class GlobalDtos {

    public static MovieDTO convertMovieToMovieDto(Movie movie){

        return  MovieDTO.builder()
                .name(movie.getName())
                .genre(movie.getGenre())
                .description(movie.getDescription())
                .language(movie.getLanguage())
                .release_date(movie.getRelease_date())
                .duration(movie.getDuration())
                .build();
    }

    public static TheaterDTO convertTheaterToTheaterDto(Theater theater){

        return  TheaterDTO.builder()
                .theaterCapacity(theater.getTheaterCapacity())
                .theaterLocation(theater.getTheaterLocation())
                .theaterName(theater.getTheaterName())
                .theaterScreenType(theater.getTheaterScreenType())
                .build();
    }


    public static ShowDTO convertShowToShowDTO(Show show){

        return  ShowDTO.builder()
                .showTime(show.getShowTime())
                .price(show.getPrice())
                .theater_id(show.getTheater().getId())
                .movie_id(show.getMovie().getId())
                .build();
    }

    public static BookingDto convertBookingToBookingDTO(Booking booking){
        return  BookingDto.builder()
                .bookingDate(booking.getBookingDate())
                .numberOfSeats(booking.getNumberOfSeats())
                .seatNumbers(booking.getSeatNumbers())
                .showId(booking.getShow().getId())
                .userId(booking.getUser().getId())
                .build();
    }

    public static PaginationMovieDto<MovieDTO> convertMovieToPaginationMovie(Page<Movie> movies){
        return PaginationMovieDto.<MovieDTO>builder()
                .content(movies.stream().map(GlobalDtos::convertMovieToMovieDto).toList())
                .last(movies.isLast())
                .pageNumber(movies.getNumber())
                .pageSize(movies.getSize())
                .totalElements(movies.getNumberOfElements())
                .totalPages(movies.getTotalPages())
                .build();
    }

}
