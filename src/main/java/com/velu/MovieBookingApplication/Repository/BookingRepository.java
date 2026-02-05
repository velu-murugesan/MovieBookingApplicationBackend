package com.velu.MovieBookingApplication.Repository;


import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    Page<Booking> findByUserId(Long id,PageRequest pageRequest);

    Page<Booking> findByShowId(Long id,PageRequest pageRequest);


    Page<Booking> findBookingsByBookingStatus(PageRequest pageRequest, BookingStatus status);


    Page<Booking> findByUserIdAndBookingStatus(Long id,PageRequest pageRequest,BookingStatus bookingStatus);

    Page<Booking> findAll(PageRequest pageRequest);

    Page<Booking> findAll(PageRequest pageRequest, Long id);
}
