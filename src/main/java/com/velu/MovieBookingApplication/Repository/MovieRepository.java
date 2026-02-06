package com.velu.MovieBookingApplication.Repository;
import com.velu.MovieBookingApplication.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    Page<Movie> findByGenre(String genre,PageRequest pageRequest);

    Page<Movie> findByLanguage(String language,PageRequest pageRequest);

    Page<Movie> findByName(PageRequest pageRequest ,String title);

    Page<Movie> findAll(PageRequest pageRequest);

    boolean isExistsById(Long id);

}
