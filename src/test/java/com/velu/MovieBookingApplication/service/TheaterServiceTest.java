package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.TheaterRepository;
import com.velu.MovieBookingApplication.dto.TheaterDTO;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Show;
import com.velu.MovieBookingApplication.entity.Theater;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;
    @InjectMocks
    private TheaterService theaterService;
    private TheaterDTO theaterDTO;
    private Theater theater;
    private PaginationResponse paginationResponse;


    @BeforeEach
    public void setup(){
        theaterDTO = TheaterDTO.builder()
                .theaterCapacity(1000)
                .theaterLocation("chennai")
                .theaterName("abc")
                .theaterScreenType("full screen")
                .build();


        List<Show> list = new ArrayList<>();

        theater = Theater.builder()
                .id(1L)
                .show(list)
                .theaterCapacity(1000)
                .theaterLocation("chennai")
                .theaterName("abc")
                .theaterScreenType("full screen")
                .build();

    }

    @Test
    public void addTheaterSuccess(){
        String theaterName = "abc";
        String theaterLocation = "chennai";
        String theaterScreenType = "full screen";
        when(theaterRepository.existsByTheaterNameAndTheaterLocationAndTheaterScreenType(theaterName,theaterLocation,theaterScreenType)).thenReturn(false);
        when(theaterRepository.save(any(Theater.class))).thenReturn(theater);
        Theater result =  theaterService.addTheater(theaterDTO);
        System.out.println(result);
        assertNotNull(result);
        log.info("theater is tested");
    }

    @Test
    public void addTheaterFailure(){
        String theaterName = "abc";
        String theaterLocation = "chennai";
        String theaterScreenType = "full screen";
        when(theaterRepository.existsByTheaterNameAndTheaterLocationAndTheaterScreenType(theaterName,theaterLocation,theaterScreenType)).thenReturn(true);
        assertThrows(ResponseStatusException.class,() -> theaterService.addTheater(theaterDTO));
        log.info("failure is handled properly");
    }

    @Test
    public void updateTheaterSuccess(){
           Long id = 1L;
           when(theaterRepository.findById(id)).thenReturn(Optional.of(theater));
           when(theaterRepository.save(any(Theater.class))).thenReturn(theater);
           Theater theater = theaterService.updateTheater(id,theaterDTO);
           assertNotNull(theater);
           log.info("theater is update");
    }

    @Test
    public void updateTheaterFailure(){
        Long id = 1L;
        when(theaterRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,() -> theaterService.updateTheater(id,theaterDTO));
        log.info("update theater failure handled properly");
    }

    @Test
    public void getTheaterByLocationSuccess(){
         Page<Theater> page = new PageImpl<>(List.of(theater));
         when(theaterRepository.existsByTheaterLocation("chennai")).thenReturn(true);
         when(theaterRepository.findByTheaterLocation(any(PageRequest.class),eq("chennai"))).thenReturn(page);
         PaginationResponse<Theater> theater = theaterService.getTheaterByLocation(0,10,"chennai");
         assertNotNull(theater);
         assertEquals(theater.getContent().getFirst().getTheaterName(),"abc");
         verify(theaterRepository,times(1)).existsByTheaterLocation("chennai");
         verify(theaterRepository,times(1)).findByTheaterLocation(any(PageRequest.class),eq("chennai"));
         log.info("theater received by location");
    }


    @Test
    public void getTheaterByLocationFailure(){
         assertThrows(ResponseStatusException.class,() -> theaterService.getTheaterByLocation(0,2,""));
    }

    @Test
    public void deleteTheaterSuccess(){
         Long id = 1L;
         when(theaterRepository.existsById(id)).thenReturn(true);
         theaterService.deleteTheater(id);
         verify(theaterRepository,times(1)).deleteById(id);
         log.info("delete is tested successfully");
    }


}
