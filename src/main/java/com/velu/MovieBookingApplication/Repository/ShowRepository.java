package com.velu.MovieBookingApplication.Repository;


import com.velu.MovieBookingApplication.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
//    Optional<List<Show>> findAllShowsByMovie(String movie);
//
//    Optional<List<Show>> findAllShowsByTheater(String theater);

    Optional<List<Show>> findByMovieId(Long movieId);

    Optional<List<Show>> findByTheaterId(Long theaterId);
}
