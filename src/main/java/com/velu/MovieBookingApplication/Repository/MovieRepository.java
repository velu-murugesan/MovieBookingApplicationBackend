package com.velu.MovieBookingApplication.Repository;
import com.velu.MovieBookingApplication.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    Page<Movie> findMoviesByGenre(String genre,PageRequest pageRequest);

    Page<Movie> findMoviesByLanguage(String language,PageRequest pageRequest);

    Page<Movie> findMoviesByName(PageRequest pageRequest ,String title);

    Page<Movie> findMoviesByLanguageAndGenre(PageRequest pageRequest,String language,String genre);

    Page<Movie> findMoviesByNameAndGenre(PageRequest pageRequest, String title, String genre);

    Page<Movie> findMoviesByLanguageAndName(PageRequest pageRequest, String language, String title);
}
