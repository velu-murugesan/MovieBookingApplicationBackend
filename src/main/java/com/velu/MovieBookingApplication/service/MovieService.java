package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.util.Utils;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

   public Movie addMovie(MovieDTO movieDTO){

         Movie movie = new Movie();
         movie.setName(movieDTO.getName());
         movie.setDescription(movieDTO.getDescription());
         movie.setGenre(movieDTO.getGenre());
         movie.setRelease_date(movieDTO.getRelease_date());
         movie.setLanguage(movieDTO.getLanguage());
         movie.setDuration(movieDTO.getDuration());

         return movieRepository.save(movie);
   }


    public Movie updateMovie(Long id,MovieDTO movieDTO) {

       Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No Movie Found for the id" + " " + id));

        System.out.println(movieDTO.getDuration());

       movie.setName(movieDTO.getName());
       movie.setDescription(movieDTO.getDescription());
       movie.setGenre(movieDTO.getGenre());
       movie.setRelease_date(movieDTO.getRelease_date());
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

        Sort sort = Sort.by(
                Sort.Order.asc("release_date")
        );


        Page<Movie> movies;

        if(name != null && genre != null && language != null){
            movies = movieRepository.findAll(pageRequest);
        }
        else if(name != null && genre != null){
            movies = movieRepository.findMoviesByNameAndGenre(pageRequest,name,genre);
        }
        else if(genre != null && language != null){
            movies = movieRepository.findMoviesByLanguageAndGenre(pageRequest,language,genre);
        }else if(language != null && name != null){
            movies = movieRepository.findMoviesByLanguageAndName(pageRequest,language,name);
        }
        else if(name != null){
            movies = movieRepository.findMoviesByName(pageRequest.withSort(sort),name);
        }
        else if(genre != null){
            movies = movieRepository.findMoviesByGenre(genre,pageRequest);
        }else if(language != null){
            movies = movieRepository.findMoviesByLanguage(language,pageRequest);
        }else{
            movies = movieRepository.findAll(pageRequest);
        }

        return Utils.convertPageToPaginationResponse(movies);
    }
}

