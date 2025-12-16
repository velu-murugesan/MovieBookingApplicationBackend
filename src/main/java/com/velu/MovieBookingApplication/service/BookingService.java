package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.BookingRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.dto.BookingDto;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.User;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(BookingDto bookingDto) {

           Show show = showRepository.findById(bookingDto.getShowId()).orElseThrow(() -> new CustomException("Show not found" + bookingDto.getShowId()));

            if(!isSeatAvailable(bookingDto.getShowId(),bookingDto.getNumberOfSeats())){
                throw  new SeatsNotAvailableException("Not enough seat are available");
            }

            if(bookingDto.getSeatNumbers().size() != bookingDto.getNumberOfSeats()){
                  throw new InvalidSeatSelectionException("Seat Numbers and Number of Seats must be equal");
            }

            validateDuplicateSeats(show.getId(),bookingDto.getSeatNumbers());

            User user =  userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new RuntimeException("No user id is found" + " " + bookingDto.getUserId()));

            Booking booking = new Booking();
            booking.setBookingDate(bookingDto.getBookingDate());
            booking.setPrice(calculateTotalAmount(show.getPrice(),bookingDto.getNumberOfSeats()));
            booking.setBookingStatus(BookingStatus.PENDING);
            booking.setShow(show);
            booking.setUser(user);
            booking.setSeatNumbers(bookingDto.getSeatNumbers());
            booking.setNumberOfSeats(bookingDto.getNumberOfSeats());

            return bookingRepository.save(booking);
    }

    private Double calculateTotalAmount(Double price, Integer numberOfSeats) {
        return price * numberOfSeats;
    }


    private void validateDuplicateSeats(Long id, List<String> seatNumbers) {
        Show show = showRepository.findById(id).orElseThrow(() -> new CustomException("Show not found" + id));

    Set<String> occupiedSeats  =    show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .flatMap(booking ->  booking.getSeatNumbers().stream())
                .collect(Collectors.toSet());

    List<String> duplicateSeats =  seatNumbers.stream()
                              .filter(occupiedSeats::contains)
                              .collect(Collectors.toList());
    if(!duplicateSeats.isEmpty()){
        throw new DuplicateSeatException("Seats are already booked");
    }

    }

    private boolean isSeatAvailable(Long showId, Integer numberOfSeats) {


        Show show = showRepository.findById(showId).orElseThrow(() -> new CustomException("Show not found" + showId));

     int bookedSeats =  show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .mapToInt(Booking :: getNumberOfSeats)
                .sum();

      return (show.getTheater().getTheaterCapacity() - bookedSeats ) >= numberOfSeats;

    }

    public Booking updateBooking(Long id, BookingDto bookingDto) {

        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new CustomException("User is not found for this id" + " " + bookingDto.getUserId()));
        Show show = showRepository.findById(bookingDto.getShowId()).orElseThrow(() -> new CustomException("Show not found" + bookingDto.getShowId()));
         Booking booking = bookingRepository.findById(id).orElseThrow(() -> new CustomException("Booking is not available in this id" + " " + id));
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setShow(show);
        booking.setUser(user);
        booking.setSeatNumbers(bookingDto.getSeatNumbers());
        booking.setNumberOfSeats(bookingDto.getNumberOfSeats());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long id) {
         return bookingRepository.findByUserId(id);
    }

    public List<Booking> getShowBookings(Long id) {
        return bookingRepository.findByShowId(id);
    }

    public Booking confirmBooking(Long id) {

       Booking booking =  bookingRepository.findById(id)
                  .orElseThrow(() -> new CustomException("Booking not found"));

       if(booking.getBookingStatus() != BookingStatus.PENDING){
            throw new InvalidBookingStateException("Booking is not pending state");
       }

       booking.setBookingStatus(BookingStatus.CONFIRMED);

       return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long id) {

        Booking booking =  bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException("Booking not found"));

        validateCancellation(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public void validateCancellation(Booking booking){
        LocalDateTime showTime = booking.getShow().getShowTime();
        LocalDateTime deadLineTime =  showTime.minusHours(2);

//         show time is for example 9:00 clock then i can cancel the show at 7:00 clock
//         7:00 clock is the cancellation time

        if(LocalDateTime.now().isAfter(deadLineTime)){
            throw new InvalidBookingCancellationException("Booking cancel can be done 2 hours before show begin");
        }

        if(booking.getBookingStatus() == BookingStatus.CANCELLED){
            throw new InvalidBookingCancellationException("Booking Already been cancelled");
        }
    }

    public List<Booking> getBookingbyUseridandStatus(Long userId,BookingStatus status) {

         return  bookingRepository.findByUserIdAndBookingStatus(userId,status);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getAllBookingsByStatus(BookingStatus status) {
       return bookingRepository.findBookingsByBookingStatus(status);
    }
}
