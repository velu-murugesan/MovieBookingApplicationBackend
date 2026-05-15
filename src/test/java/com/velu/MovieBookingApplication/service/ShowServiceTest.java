package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.Repository.ShowRepository;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dto.ShowDTO;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Booking;
import com.velu.MovieBookingApplication.entity.Movie;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.Theater;
import com.velu.MovieBookingApplication.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class ShowServiceTest {

    @Mock
   ShowRepository showRepository;
    @Mock
   MovieRepository movieRepository;
    @Mock
   TheaterRepository theaterRepository;
    @InjectMocks
    ShowService showService;
   Show show;
   ShowDTO showDTO;
   PaginationResponse<Show> paginationResponse;
   Page<Show> shows;
   PageRequest pageRequest;
   Movie movie;
   Theater theater;
   List<Show> showList;

   @BeforeEach
    void setup(){

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

       showDTO = ShowDTO.builder()
               .movie_id(1L)
               .showTime(LocalDateTime.now())
               .theater_id(1L)
               .price(600.00)
               .build();

       showList = List.of(show);
       shows = new PageImpl<>(showList);
       paginationResponse = Utils.convertPageToPaginationResponse(shows);
       pageRequest = PageRequest.of(0,2);

   }

   @Test
   void createShowSuccess(){
       when(showRepository.save(any(Show.class))).thenReturn(show);
       when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
       when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
       when(showRepository.existsByShowTimeAndMovieAndTheater(showDTO.getShowTime(),movie,theater)).thenReturn(false);
       Show show = showService.createShow(showDTO);
       assertNotNull(show);
       assertEquals("X-man",show.getMovie().getName());
       assertEquals("abc",show.getTheater().getTheaterName());
       assertEquals(600.00,show.getPrice());
       verify(showRepository,times(1)).save(any(Show.class));
       verify(theaterRepository,times(1)).findById(1L);
       verify(movieRepository,times(1)).findById(1L);

   }

    @Test
    void createShowFailure(){
       when(movieRepository.findById(1L)).thenReturn(Optional.empty());
       ResponseStatusException res = assertThrows(ResponseStatusException.class,() -> showService.createShow(showDTO));
       assertEquals("404 NOT_FOUND \"No movie found for this id 1\"",res.getMessage());
        verify(movieRepository, times(1))
                .findById(1L);

        verify(theaterRepository, never()).findById(anyLong());
        verify(showRepository, never()).save(any(Show.class));
    }


    @Test
    void updateShowSuccess(){
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showRepository.save(any(Show.class))).thenReturn(show);
         Show updatedShow = showService.updateShow(1L,showDTO);

         assertNotNull(updatedShow);
         assertEquals("X-man",updatedShow.getMovie().getName());
         assertEquals("abc",updatedShow.getTheater().getTheaterName());
         verify(theaterRepository,times(1)).findById(1L);
         verify(movieRepository,times(1)).findById(1L);
         verify(showRepository,times(1)).findById(1L);
         verify(showRepository,times(1)).save(any(Show.class));
    }

    @Test
    void updateShowFailure(){
        when(showRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException res = assertThrows(ResponseStatusException.class,() -> showService.updateShow(1L,showDTO));
        assertEquals("404 NOT_FOUND \"No movie found for this id 1\"",res.getMessage());
        verify(movieRepository,never()).findById(anyLong());
        verify(showRepository,never()).save(any(Show.class));
        verify(theaterRepository,never()).findById(anyLong());
    }

    @Test
    void deleteShowSuccess(){
       when(showRepository.existsById(1L)).thenReturn(true);
       List<Booking> bookings = new ArrayList<>();
       Show show = new Show();
       show.setBookings(bookings);
       when(showRepository.findById(1L)).thenReturn(Optional.of(show));
       List<Booking> bookings1 = showRepository.findById(1L).get().getBookings();
       showService.deleteShow(1L);
       assertEquals(bookings,bookings1);
    }

    @Test
    void deleteShowFailure(){
        when(showRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class,() -> showService.deleteShow(1L));
    }

    @Test
    void getAllShowsByMovieAndTheaterSuccess(){
          when(showRepository.findShowsByMovieAndTheater(pageRequest,"X-man","abc")).thenReturn(shows);
          try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
              when(Utils.convertPageToPaginationResponse(shows)).thenReturn(paginationResponse);
              PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"X-man","abc");
              assertEquals("X-man",shows.getContent().getFirst().getMovie().getName());
          }
    }

    @Test
    void getAllShowsByMovieAndTheaterFailure(){
        when(showRepository.findShowsByMovieAndTheater(pageRequest,"X-man","abc")).thenReturn(new PageImpl<>(new ArrayList<>()));
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(new PageImpl<>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"X-man","abc");
            assertNotNull(shows);
            assertNull(shows.getContent());
        }
    }

    @Test
    void getAllShowsByMovieSuccess(){
        when(showRepository.findShowsByMovie(pageRequest,"X-man")).thenReturn(shows);
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(shows)).thenReturn(paginationResponse);
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"X-man","");
            assertNotNull(shows);
            assertEquals("X-man",shows.getContent().getFirst().getMovie().getName());
        }
    }

    @Test
    void getAllShowsByMovieFailure(){
        when(showRepository.findShowsByMovie(pageRequest,"X-man")).thenReturn(new PageImpl<>(new ArrayList<>()));
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(new PageImpl<>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"X-man","");
            assertNotNull(shows);
            assertNull(shows.getContent());
        }
    }

    @Test
    void getAllShowsByTheaterSuccess(){
        when(showRepository.findShowsByTheater(pageRequest,"abc")).thenReturn(shows);
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(shows)).thenReturn(paginationResponse);
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"","abc");
            assertNotNull(shows);
            assertEquals("abc",shows.getContent().getFirst().getTheater().getTheaterName());
        }
    }

    @Test
    void getAllShowsByTheaterFailure(){
        when(showRepository.findShowsByTheater(pageRequest,"abc")).thenReturn(new PageImpl<>(new ArrayList<>()));
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(new PageImpl<Object>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"","abc");
            assertNotNull(shows);
            assertNull(shows.getContent());
        }
    }


    @Test
    void getAllShowsSuccess(){
        when(showRepository.findAll(pageRequest)).thenReturn(shows);
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(shows)).thenReturn(paginationResponse);
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"","");
            assertNotNull(shows);
            assertEquals("abc",shows.getContent().getFirst().getTheater().getTheaterName());
        }
    }

    @Test
    void getAllShowsFailure(){
        when(showRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(new PageImpl<>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Show> shows = showService.getAllShows(pageRequest,"","");
            assertNotNull(shows);
            assertNull(shows.getContent());
        }
    }

}
