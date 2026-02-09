package com.velu.MovieBookingApplication.Repository;
import com.velu.MovieBookingApplication.entity.Show;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {

    Page<Show> findShowsByMovie(PageRequest pageRequest,String movie);
    Page<Show> findShowsByTheater(PageRequest pageRequest,String theater);
    Page<Show> findShowsByMovieAndTheater(PageRequest pageRequest,String movie,String theater);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Show s WHERE s.id = :showId")
   Show findByIdForUpdate(@Param("showId") Long showId);
}
