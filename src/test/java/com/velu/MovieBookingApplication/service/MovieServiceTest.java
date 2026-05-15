package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.entity.Movie;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
class MovieServiceTest {

    Movie movie;
    MovieDTO movieDTO;
    PageRequest pageRequest ;

    static String name = "X-man";
    static String language = "english";
    static String genre = "adventure";
    List<Movie> movieList;
    Page<Movie> movies;
    PaginationResponse<Movie> paginationResponse;
    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieService movieService;

    @BeforeEach
    void setup(){
        movie = Movie.builder()
                .name("X-man")
                .description("good movie")
                .show(new ArrayList<>())
                .duration(2)
                .language("english")
                .genre("adventure")
                .releaseDate(LocalDate.now())
                .id(1L)
                .build();
         movieList = List.of(movie);
         movies = new PageImpl<>(movieList);
         paginationResponse = Utils.convertPageToPaginationResponse(movies);
         movieDTO = MovieDTO.builder()
                .name("X-man")
                .description("good movie")
                .duration(2)
                .language("english")
                .genre("adventure")
                .releaseDate(LocalDate.now())
                .build();

        pageRequest = PageRequest.of(0,2);


    }

    @Test
    void addMovieSuccess() {
           when((movieRepository.
                   existsByNameAndLanguageAndGenreAndReleaseDate(movieDTO.getName(), movieDTO.getLanguage(), movieDTO.getGenre(), movieDTO.getReleaseDate()))).thenReturn(false);
           when(movieRepository.save(any(Movie.class))).thenReturn(movie);

            Movie movie =  movieService.addMovie(movieDTO);

            assertNotNull(movie);
            assertEquals(movie.getName(),"X-man");

            verify(movieRepository, times(1)).existsByNameAndLanguageAndGenreAndReleaseDate(movieDTO.getName(),movieDTO.getLanguage(),movieDTO.getGenre(),movieDTO.getReleaseDate());
            log.info("movie success tested successfully");
    }


    @Test
    void addMovieFailure(){
        when((movieRepository.
                existsByNameAndLanguageAndGenreAndReleaseDate(movieDTO.getName(), movieDTO.getLanguage(), movieDTO.getGenre(), movieDTO.getReleaseDate()))).thenReturn(true);
        assertThrows(ResponseStatusException.class,() -> movieService.addMovie(movieDTO));
        log.info("movie failure tested successfully");
    }

    @Test
    void updateMovieSuccess() {
          Movie movie1 = Movie.builder()
                  .name("bird man")
                  .show(new ArrayList<>())
                  .genre("magical realism")
                  .language("spanish")
                  .duration(2)
                  .releaseDate(LocalDate.now())
                  .build();
          when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));
          when(movieRepository.save(any(Movie.class))).thenReturn(movie);

          Movie movie2 = movieService.updateMovie(1L,movieDTO);

          assertNotEquals(movie2,"movie2 is not null");
          assertEquals("X-man",movie2.getName());
          assertEquals("english",movie2.getLanguage());

          verify(movieRepository,times(1)).findById(1L);
          verify(movieRepository,times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovieFailure() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,() -> movieService.updateMovie(1L,movieDTO));
    }

    @Test
    void deleteMovieSuccess() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        movieService.deleteMovie(1L);
        verify(movieRepository,times(1)).existsById(1L);
    }


    @Test
    void deleteMovieFailure() {
        when(movieRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class,() -> movieService.deleteMovie(1L));
        verify(movieRepository,times(1)).existsById(1L);
    }

    
    @Test
    void getAllMoviesByNameAndGenreSuccess(){
        when(movieRepository.findMoviesByNameAndGenre(pageRequest,name,genre)).thenReturn(movies);
       try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
           mocked.when(() -> Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
           PaginationResponse<Movie> paginationResponse1 =  movieService.getAllMovies(pageRequest,"",genre,name);
           assertNotNull(paginationResponse1);
           assertEquals("X-man",paginationResponse1.getContent().getFirst().getName());
           verify(movieRepository,times(1)).findMoviesByNameAndGenre(pageRequest,name,genre);
       }
    }

    @Test
    void getAllMoviesByNameAndGenreFailure(){
        when(movieRepository.findMoviesByNameAndGenre(pageRequest,name,genre)).thenReturn(new PageImpl<>(new ArrayList<>()));
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(new PageImpl<Movie>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Movie> paginationResponse1 =  movieService.getAllMovies(pageRequest,language,genre,name);
            assertNotNull(paginationResponse1);
            assertNull(paginationResponse1.getContent());
            verify(movieRepository,times(1)).findMoviesByNameAndGenre(pageRequest,name,genre);
        }
    }

    @Test
    void getAllMoviesByLanguageAndNameSuccess(){
        when(movieRepository.findMoviesByLanguageAndName(pageRequest,language,name)).thenReturn(movies);
        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
            PaginationResponse<Movie> paginationResponse1 = movieService.getAllMovies(pageRequest,language,"",name);
            assertNotNull(paginationResponse1);
            assertEquals("X-man",paginationResponse1.getContent().getFirst().getName());
            verify(movieRepository,times(1)).findMoviesByLanguageAndName(pageRequest,language,name);
        }
    }

    @Test
    void getAllMoviesByLanguageAndNameFailure(){
        when(movieRepository.findMoviesByLanguageAndName(pageRequest,language,name)).thenReturn(new PageImpl<>(new ArrayList<>()));

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(new PageImpl<>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Movie> paginationResponse1 = movieService.getAllMovies(pageRequest,language,"",name);
            assertNotNull(paginationResponse1);
            assertNull(paginationResponse1.getContent());
            verify(movieRepository,times(1)).findMoviesByLanguageAndName(pageRequest,language,name);
        }

    }

    @Test
    void getAllMoviesByGenreAndLanguageSuccess(){
        when(movieRepository.findMoviesByLanguageAndGenre(pageRequest,language,genre)).thenReturn(movies);

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
            PaginationResponse<Movie> paginationResponse1 = movieService.getAllMovies(pageRequest,language,genre,"");
            assertNotNull(paginationResponse1);
            assertEquals("X-man",paginationResponse1.getContent().getFirst().getName());
            verify(movieRepository,times(1)).findMoviesByLanguageAndGenre(pageRequest,language,genre);
        }

    }


    @Test
    void getAllMoviesByGenreAndLanguageFailure(){
        when(movieRepository.findMoviesByLanguageAndGenre(pageRequest,language,genre)).thenReturn(new PageImpl<>(new ArrayList<>()));

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(new PageImpl<Movie>(new ArrayList<>()))).thenReturn(new PaginationResponse<Movie>());
            PaginationResponse<Movie> paginationResponse1 = movieService.getAllMovies(pageRequest,language,genre,"");
            assertNotNull(paginationResponse1);
            assertNull(paginationResponse1.getContent());
            verify(movieRepository,times(1)).findMoviesByLanguageAndGenre(pageRequest,language,genre);
        }

    }

    @Test
    void getAllMoviesByNameSuccess(){
         when(movieRepository.findMoviesByName(pageRequest,name)).thenReturn(movies);

         try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
               when(Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
             PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,"","",name);
             assertNotNull(movies);
             assertEquals("X-man",movies.getContent().getFirst().getName());
             verify(movieRepository,times(1)).findMoviesByName(pageRequest,name);
         }
    }

    @Test
    void getAllMoviesByNameFailure(){
        when(movieRepository.findMoviesByName(pageRequest,name)).thenReturn(new PageImpl<>(new ArrayList<>()));

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(new PageImpl<>(new ArrayList<>()))).thenReturn(new PaginationResponse<Movie>());
            PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,"","",name);
            assertNotNull(movies);
            assertNull(movies.getContent());
            verify(movieRepository,times(1)).findMoviesByName(pageRequest,name);
        }
    }

    @Test
    void getAllMoviesByGenreSuccess(){
        when(movieRepository.findMoviesByGenre(genre,pageRequest)).thenReturn(movies);

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
            PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,"",genre,"");
            assertNotNull(movies);
            assertEquals("X-man",movies.getContent().getFirst().getName());
            verify(movieRepository,times(1)).findMoviesByGenre(genre,pageRequest);
            log.info(movies.getContent().getFirst().getName());
        }
    }

    @Test
    void getAllMoviesByGenreFailure(){
        when(movieRepository.findMoviesByGenre(genre,pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            mocked.when(() -> Utils.convertPageToPaginationResponse(new PageImpl<Movie>(new ArrayList<>()))).thenReturn(new PaginationResponse<Movie>());
            PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,"",genre,"");
            assertNotNull(movies);
            assertNull(movies.getContent());
            verify(movieRepository,times(1)).findMoviesByGenre(genre,pageRequest);
        }
    }

    @Test
    void getAllMoviesByLanguageSuccess(){
        when(movieRepository.findMoviesByLanguage(language,pageRequest)).thenReturn(movies);

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
            PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,language,"","");
            assertNotNull(movies);
            assertEquals("X-man",movies.getContent().getFirst().getName());
            verify(movieRepository,times(1)).findMoviesByLanguage(language,pageRequest);
            log.info(movies.getContent().getFirst().getName());
        }
    }

    @Test
    void getAllMoviesByLanguageFailure(){
        when(movieRepository.findMoviesByLanguage(language,pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));

        try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(new PageImpl<>(new ArrayList<>()))).thenReturn(new PaginationResponse<>());
            PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,language,"","");
            assertNotNull(movies);
            assertNull(movies.getContent());
            verify(movieRepository,times(1)).findMoviesByLanguage(language,pageRequest);
        }
    }


    @Test
    void getAllMovies(){
          when(movieRepository.findAll(pageRequest)).thenReturn(movies);
           try(MockedStatic<Utils> mocked = mockStatic(Utils.class)){
            when(Utils.convertPageToPaginationResponse(movies)).thenReturn(paginationResponse);
            PaginationResponse<Movie> movies =  movieService.getAllMovies(pageRequest,"","","");
            assertNotNull(movies);
            assertEquals("X-man",movies.getContent().getFirst().getName());
            verify(movieRepository,times(1)).findAll(pageRequest);
        }
    }

}