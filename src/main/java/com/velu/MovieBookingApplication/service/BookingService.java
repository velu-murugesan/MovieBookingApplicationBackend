package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.BookingRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.dto.PaginationResponse;
import com.velu.MovieBookingApplication.util.Utils;
import com.velu.MovieBookingApplication.dto.BookingDto;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.User;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public BookingDto createBooking(BookingDto bookingDto) {

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

            return Utils.convertBookingToBookingDTO(bookingRepository.save(booking));
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

    public BookingDto updateBooking(Long id, BookingDto bookingDto) {

        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new CustomException("User is not found for this id" + " " + bookingDto.getUserId()));
        Show show = showRepository.findById(bookingDto.getShowId()).orElseThrow(() -> new CustomException("Show not found" + bookingDto.getShowId()));
         Booking booking = bookingRepository.findById(id).orElseThrow(() -> new CustomException("Booking is not available in this id" + " " + id));
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setShow(show);
        booking.setUser(user);
        booking.setSeatNumbers(bookingDto.getSeatNumbers());
        booking.setNumberOfSeats(bookingDto.getNumberOfSeats());

        return Utils.convertBookingToBookingDTO(bookingRepository.save(booking));
    }

    public PaginationResponse<BookingDto> getUserBookings(Integer page,Integer size,Long id) {
        Sort sort = Sort.by(
             Sort.Order.asc("show.showTime"),
             Sort.Order.desc("createdAt")
        );


        PageRequest pageRequest = PageRequest.of(page,size,sort);

        Page<Booking> bookings = bookingRepository.findAll(pageRequest,id);

        return Utils.convertPageToPaginationResponse(bookings, Utils::convertBookingToBookingDTO);
    }


    public PaginationResponse<BookingDto> getShowBookings(Integer page, Integer size, Long id ) {

        Sort sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );


        PageRequest pageRequest = PageRequest.of(page,size,sort);

        Page<Booking> bookings =  bookingRepository.findByShowId(id,pageRequest);

        return  Utils.convertPageToPaginationResponse(bookings,Utils::convertBookingToBookingDTO);
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

    public BookingDto cancelBooking(Long id) {

        Booking booking =  bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException("Booking not found"));

        validateCancellation(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        return Utils.convertBookingToBookingDTO(bookingRepository.save(booking));
    }

    private void validateCancellation(Booking booking){
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

    public PaginationResponse<BookingDto> getBookingbyUseridandStatus(Integer offset,Integer pageSize,Long id,BookingStatus status) {

        Sort sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );


        PageRequest pageRequest = PageRequest.of(offset,pageSize,sort);

        Page<Booking> bookings;

        if(status == null){
           bookings =  bookingRepository.findByUserId(id,pageRequest);
        }

        bookings =   bookingRepository.findByUserIdAndBookingStatus(id,pageRequest,status);
       return Utils.convertPageToPaginationResponse(bookings,Utils::convertBookingToBookingDTO);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public PaginationResponse<BookingDto> getAllBookings(Integer page, Integer size,  BookingStatus status) {

        Sort sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );


        PageRequest pageRequest = PageRequest.of(page,size,sort);

        Page<Booking> bookings;

        if(status != null){
            bookings = bookingRepository.findBookingsByBookingStatus(pageRequest,status);
        }

       bookings =  bookingRepository.findAll(pageRequest);

       return Utils.convertPageToPaginationResponse(bookings,Utils::convertBookingToBookingDTO);

    }


}
