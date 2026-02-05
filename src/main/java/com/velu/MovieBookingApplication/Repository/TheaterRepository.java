package com.velu.MovieBookingApplication.Repository;

import com.velu.MovieBookingApplication.entity.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

   Page<Theater> findByTheaterLocation(PageRequest pageRequest,String location);
   Boolean existsByLocation(String location);
}
