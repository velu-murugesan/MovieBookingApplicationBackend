package com.velu.MovieBookingApplication.Repository;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.Theater;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {

    Page<Show> findShowsByMovie(PageRequest pageRequest,String movie);
    Page<Show> findShowsByTheater(PageRequest pageRequest,String theater);
    Page<Show> findShowsByMovieAndTheater(PageRequest pageRequest,String movie,String theater);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Show s WHERE s.id = :showId")
    Optional<Show> findByIdForUpdate(@Param("showId") Long showId);
    Boolean existsByShowTimeAndMovieAndTheater(LocalDateTime showTime, Movie movie, Theater theater);
}
