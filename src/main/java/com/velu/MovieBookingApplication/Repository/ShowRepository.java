package com.velu.MovieBookingApplication.Repository;


import com.velu.MovieBookingApplication.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {

    Page<Show> findAll(PageRequest pageRequest,String movie);
    Page<Show> findAll(String genre,PageRequest pageRequest);
    Page<Show> findAll(PageRequest pageRequest);
}
