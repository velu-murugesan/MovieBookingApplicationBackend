package com.velu.MovieBookingApplication.util;

import com.velu.MovieBookingApplication.dto.*;
import com.velu.MovieBookingApplication.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class Utils {

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



    public static<E,D> PaginationResponse<D> convertPageToPaginationResponse(
            Page<E> movies,
            Function<E,D> converter
    ){
        return PaginationResponse.<D>builder()

                .content(movies.stream().map(converter).toList())
                .last(movies.isLast())
                .pageNumber(movies.getNumber())
                .pageSize(movies.getSize())
                .totalElements(movies.getNumberOfElements())
                .totalPages(movies.getTotalPages())
                .build();
    }


}
