package com.velu.MovieBookingApplication.Repository;


import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByUserId(Long id);

    List<Booking> findByShowId(Long id);


    List<Booking> findBookingsByBookingStatus(BookingStatus bookingStatus);

    List<Booking> findByUserIdAndBookingStatus(Long userId, BookingStatus status);
}
