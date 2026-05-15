package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.util.Utils;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

   public Movie addMovie(MovieDTO movieDTO){

         if(movieRepository.existsByNameAndLanguageAndGenreAndReleaseDate(movieDTO.getName(),movieDTO.getLanguage(),movieDTO.getGenre(),movieDTO.getReleaseDate())){
             throw new ResponseStatusException(HttpStatus.CONFLICT,"movie is already exist");
         }

         Movie movie = new Movie();
         movie.setName(movieDTO.getName());
         movie.setDescription(movieDTO.getDescription());
         movie.setGenre(movieDTO.getGenre());
         movie.setReleaseDate(movieDTO.getReleaseDate());
         movie.setLanguage(movieDTO.getLanguage());
         movie.setDuration(movieDTO.getDuration());
         return movieRepository.save(movie);
   }


    public Movie updateMovie(Long id,MovieDTO movieDTO) {

       Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No Movie Found for the id" + " " + id));


       movie.setName(movieDTO.getName());
       movie.setDescription(movieDTO.getDescription());
       movie.setGenre(movieDTO.getGenre());
       movie.setReleaseDate(movieDTO.getReleaseDate());
       movie.setDuration(movieDTO.getDuration());
       movie.setLanguage(movieDTO.getLanguage());

      return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {

       if(!movieRepository.existsById(id)){
           throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"No Movie found for this id");
       }
      movieRepository.deleteById(id);
    }

    public PaginationResponse<Movie> getAllMovies(PageRequest pageRequest, String language, String genre, String name) {

        Page<Movie> movies;

       if(name != null && genre != null && !name.isBlank() && !genre.isBlank()){
           System.out.println("1");
            movies = movieRepository.findMoviesByNameAndGenre(pageRequest,name,genre);
        }
        else if(genre != null && language != null && !language.isBlank() && !genre.isBlank()){
           System.out.println("2");
            movies = movieRepository.findMoviesByLanguageAndGenre(pageRequest,language,genre);
        }else if(language != null && name != null && !name.isBlank() && !language.isBlank()){
            System.out.println("3");
            movies = movieRepository.findMoviesByLanguageAndName(pageRequest,language,name);
        }
        else if(name != null && !name.isBlank()){
           System.out.println("4");
            movies = movieRepository.findMoviesByName(pageRequest,name);
        }
        else if(genre != null && !genre.isBlank()){
           System.out.println("5");
            movies = movieRepository.findMoviesByGenre(genre,pageRequest);
        }else if(language != null && !language.isBlank()){
           System.out.println("6");
            movies = movieRepository.findMoviesByLanguage(language,pageRequest);
        }else{
           System.out.println("7");
            movies = movieRepository.findAll(pageRequest);
        }

        return Utils.convertPageToPaginationResponse(movies);
    }
}

