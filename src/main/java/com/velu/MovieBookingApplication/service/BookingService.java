package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.BookingRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.util.Utils;
import com.velu.MovieBookingApplication.dto.BookingDTO;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.User;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    @Transactional
    public Booking createBooking(BookingDTO bookingDto) {

                Booking booking = new Booking();

                Show show = showRepository.findByIdForUpdate(bookingDto.getShowId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"show is not found"));

                if(bookingDto.getNumberOfSeats() <= 0){
                     throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"At least one seat must be selected");
                }

                if(!isSeatAvailable(show,bookingDto.getNumberOfSeats())){
                    throw  new ResponseStatusException(HttpStatus.CONFLICT ,"Not enough seats are there");
                }

                if(bookingDto.getSeatNumbers().size() != bookingDto.getNumberOfSeats()){
                    throw new ResponseStatusException(HttpStatus.CONFLICT,"Seat Numbers and Number of Seats must be equal");
                }

                validateDuplicateSeats(show,bookingDto.getSeatNumbers());

                User user =  userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "No user id is found" + " " + bookingDto.getUserId()));

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


    public void validateDuplicateSeats(Show show, List<String> seatNumbers) {

    Set<String> occupiedSeats  =    show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .flatMap(booking ->  booking.getSeatNumbers().stream())
                .collect(Collectors.toSet());

    List<String> duplicateSeats =  seatNumbers.stream()
                              .filter(occupiedSeats::contains)
                              .toList();
    if(!duplicateSeats.isEmpty()){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Seats are already booked");
    }

    }

    public boolean isSeatAvailable(Show show, Integer numberOfSeats) {

     int bookedSeats =  show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .mapToInt(Booking :: getNumberOfSeats)
                .sum();

      return (show.getTheater().getTheaterCapacity() - bookedSeats ) >= numberOfSeats;

    }


    public Booking updateBooking(Long id, BookingDTO bookingDto) {

        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not found for this id" + " " + bookingDto.getUserId()));
        Show show = showRepository.findById(bookingDto.getShowId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Show not found" + bookingDto.getShowId()));
         Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking is not found for this id" + " " + id));
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setShow(show);
        booking.setPrice(booking.getPrice());
        booking.setUser(user);
        booking.setSeatNumbers(bookingDto.getSeatNumbers());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setNumberOfSeats(bookingDto.getNumberOfSeats());

        return bookingRepository.save(booking);
    }

    public PaginationResponse<Booking> getUserBookings(Integer page,Integer size,Long id) {
        Sort sort = Sort.by(
             Sort.Order.asc("show.showTime"),
             Sort.Order.desc("createdAt")
        );

        int maxPageSize = 20;
        int pageSize =  Math.min(maxPageSize,Math.abs(size));

        PageRequest pageRequest = PageRequest.of(page,pageSize,sort);

        Page<Booking> bookings = bookingRepository.findByUserId(pageRequest,id);

        return Utils.convertPageToPaginationResponse(bookings);
    }


    public PaginationResponse<Booking> getShowBookings(Integer page, Integer size, Long id ) {

        Sort sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );

        int maxPageSize = 20;
        int pageSize =  Math.min(maxPageSize,Math.abs(size));

        PageRequest pageRequest = PageRequest.of(page,pageSize,sort);

        Page<Booking> bookings =  bookingRepository.findByShowId(id,pageRequest);

        return  Utils.convertPageToPaginationResponse(bookings);
    }

    public Booking confirmBooking(Long id) {

       Booking booking =  bookingRepository.findById(id)
                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));

       if(booking.getBookingStatus() != BookingStatus.PENDING){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Booking is not pending state");
       }

       booking.setBookingStatus(BookingStatus.CONFIRMED);

       return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long id) {

        Booking booking =  bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));

        validateCancellation(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    private void validateCancellation(Booking booking){
        LocalDateTime showTime = booking.getShow().getShowTime();
        LocalDateTime deadLineTime =  showTime.minusHours(2);

//         show time is for example 9:00 clock then i can cancel the show at 7:00 clock
//         7:00 clock is the cancellation time

        if(LocalDateTime.now().isAfter(deadLineTime)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Booking cancel can be done 2 hours before show begin");
        }

        if(booking.getBookingStatus() == BookingStatus.CANCELLED){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Booking Already been cancelled");
        }
    }

    public PaginationResponse<Booking> getBookingbyUseridandStatus(Integer offset,Integer size,Long id,BookingStatus status) {

        Sort sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );

        int maxPageSize = 20;
        int pageSize =  Math.min(maxPageSize,Math.abs(size));

        PageRequest pageRequest = PageRequest.of(offset,pageSize,sort);

        Page<Booking> bookings;

        if(status == null){
           bookings =  bookingRepository.findByUserId(pageRequest,id);
        }

        bookings =   bookingRepository.findByUserIdAndBookingStatus(id,pageRequest,status);
       return Utils.convertPageToPaginationResponse(bookings);
    }

    public void deleteBooking(Long id) {

        if(!bookingRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking id is not found: " + id);
        }

        bookingRepository.deleteById(id);
    }

    public PaginationResponse<Booking> getAllBookings(Integer page, Integer size,  BookingStatus status) {

        Sort sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );

        int maxPageSize = 20;
        int pageSize =  Math.min(maxPageSize,Math.abs(size));

        PageRequest pageRequest = PageRequest.of(page,pageSize,sort);

        Page<Booking> bookings;

        if(status != null){
            bookings = bookingRepository.findBookingsByBookingStatus(pageRequest,status);
        }

       bookings =  bookingRepository.findAll(pageRequest);

       return Utils.convertPageToPaginationResponse(bookings);

    }


}
