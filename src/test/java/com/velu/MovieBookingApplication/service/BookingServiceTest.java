package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.BookingRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.dto.BookingDTO;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.*;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import com.velu.MovieBookingApplication.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    ShowRepository showRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    BookingService bookingService;

    Booking booking;
    BookingDTO bookingDTO;
    PaginationResponse<Booking> paginationResponse;
    Show show;
    User user;
    Movie movie;
    Theater theater;
    PageRequest pageRequest;
    Page<Booking> bookings;
    Sort sort;
    @BeforeEach
    void init(){

        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        user = User.builder()
                .email("abc@gmail.com")
                .roles(roles)
                .id(1L)
                .password("abc")
                .username("abc")
                .build();

        movie = Movie.builder()
                .name("X-man")
                .description("good movie")
                .duration(2)
                .language("english")
                .genre("adventure")
                .releaseDate(LocalDate.now())
                .id(1L)
                .build();

        theater = Theater.builder()
                .id(1L)
                .theaterCapacity(1000)
                .theaterLocation("chennai")
                .theaterName("abc")
                .theaterScreenType("full screen")
                .build();

        show = Show.builder()
                .movie(movie)
                .price(600.00)
                .showTime(LocalDateTime.of(2026,12,6,15,50))
                .bookings(new ArrayList<>())
                .theater(theater)
                .id(1L)
                .build();

        booking = Booking.builder()
                .bookingDate(LocalDateTime.now())
                .bookingStatus(BookingStatus.PENDING)
                .createdAt(LocalDateTime.of(2026,5,11,1,50))
                .numberOfSeats(2)
                .seatNumbers(List.of("A1","A2"))
                .user(user)
                .show(show)
                .id(1L)
                .price(1200.00)
                .build();
        bookingDTO = BookingDTO.builder()
                .userId(1L)
                .showId(1L)
                .seatNumbers(List.of("A1","A2"))
                .bookingDate(LocalDateTime.now())
                .numberOfSeats(2)
                .build();
        sort = Sort.by(
                Sort.Order.asc("show.showTime"),
                Sort.Order.desc("createdAt")
        );

        pageRequest = PageRequest.of(0,2);
        List<Booking> bookingList = List.of(booking);
        bookings = new PageImpl<>(bookingList);
        paginationResponse = Utils.convertPageToPaginationResponse(bookings);
    }


    @Test
    void createBookingSuccess(){
             when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
             bookingService.isSeatAvailable(show,2);
             bookingService.validateDuplicateSeats(show,List.of("A1","A2"));
             when(userRepository.findById(1L)).thenReturn(Optional.of(user));
             when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
             Booking booking1 =  bookingService.createBooking(bookingDTO);
             assertNotNull(booking1);
             assertEquals(BookingStatus.PENDING,booking1.getBookingStatus());
             verify(bookingRepository,times(1)).save(any(Booking.class));
    }

    @Test
    void createBookingFailureAtFindByIdForUpdate(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,() -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void createBookingFailureAtCheckingNumberOfSeatsIsGreaterThenOrEqualToZero(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
        BookingDTO  dto = BookingDTO.builder()
                .userId(1L)
                .showId(1L)
                .seatNumbers(List.of("A1","A2"))
                .bookingDate(LocalDateTime.now())
                .numberOfSeats(0)
                .build();

      assertThrows(ResponseStatusException.class,() -> bookingService.createBooking(dto));
    }


    @Test
    void createBookingFailureAtCheckingSeatAvailability(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
        bookingService.isSeatAvailable(show,0);
        assertThrows(ResponseStatusException.class,() -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void createBookingFailureAtCheckingTheNoOfSeatsIsEqualToSizeOfSeatNumbers(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
        BookingDTO  dto = BookingDTO.builder()
                .userId(1L)
                .showId(1L)
                .seatNumbers(List.of("A1","A2"))
                .bookingDate(LocalDateTime.now())
                .numberOfSeats(0)
                .build();
        assertThrows(ResponseStatusException.class,() -> bookingService.createBooking(bookingDTO));
        verify(showRepository,times(1)).findByIdForUpdate(1L);
    }

    @Test
    void createBookingFailureAtCheckingForValidDuplicateSeats(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
        bookingService.validateDuplicateSeats(show,List.of("A1","A2"));

        List<Booking> bookings1 = new ArrayList<>();
        bookings1.add(booking);
        Show show1 = Show.builder()
                .movie(movie)
                .price(600.00)
                .showTime(LocalDateTime.of(2026,12,6,15,50))
                .bookings(bookings1)
                .theater(theater)
                .id(1L)
                .build();

           assertThrows(ResponseStatusException.class,() -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void createBookingFailureAtFindByUserId(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,() -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void createBookingFailureAtSavingBooking(){
        when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());
        BookingDTO dto = BookingDTO.builder()
                .userId(1L)
                .showId(1L)
                .seatNumbers(List.of("A1","A2"))
                .bookingDate(LocalDateTime.now())
                .numberOfSeats(2)
                .build();
        Booking booking1 = bookingService.createBooking(dto);
        assertNotEquals(BookingStatus.PENDING,booking1.getBookingStatus());
    }

    @Test
    void updateBookingSuccess(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        Booking booking1 = bookingService.updateBooking(1L,bookingDTO);
        assertEquals(1200.00,booking1.getPrice());
    }

    @Test
    void updateBookingFailure(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
       assertThrows(ResponseStatusException.class,() -> bookingService.updateBooking(1L,bookingDTO));
    }

    @Test
    void getUserBookingsSuccess(){

    }

}
