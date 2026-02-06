package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.dto.PaginationResponse;
import com.velu.MovieBookingApplication.exception.ResourceNotFoundException;
import com.velu.MovieBookingApplication.util.Utils;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

   public MovieDTO addMovie(MovieDTO movieDTO){
         Movie movie = new Movie();
         movie.setName(movieDTO.getName());
         movie.setDescription(movieDTO.getDescription());
         movie.setGenre(movieDTO.getGenre());
         movie.setRelease_date(movieDTO.getRelease_date());
         movie.setLanguage(movieDTO.getLanguage());
         movie.setDuration(movieDTO.getDuration());

         return Utils.convertMovieToMovieDto(movieRepository.save(movie));
   }


    public MovieDTO updateMovie(Long id,MovieDTO movieDTO) {

       Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Movie Found for the id" + " " + id));

        System.out.println(movieDTO.getDuration());

       movie.setName(movieDTO.getName());
       movie.setDescription(movieDTO.getDescription());
       movie.setGenre(movieDTO.getGenre());
       movie.setRelease_date(movieDTO.getRelease_date());
       movie.setDuration(movieDTO.getDuration());
       movie.setLanguage(movieDTO.getLanguage());

      return Utils.convertMovieToMovieDto(movieRepository.save(movie));
    }

    public void deleteMovie(Long id) {

       if(!movieRepository.isExistsById(id)){
           throw  new ResourceNotFoundException("No Movie found for this id");
       }
      movieRepository.deleteById(id);
    }

    public PaginationResponse<MovieDTO> getAllMovies(PageRequest pageRequest, String language, String genre, String title) {

        Sort sort = Sort.by(
                Sort.Order.asc("release_date")
        );


        Page<Movie> movies;

        if(title != null){
            movies = movieRepository.findByName(pageRequest.withSort(sort),title);
        }else if(genre != null){
            movies = movieRepository.findByGenre(genre,pageRequest);
        }else if(language != null){
            movies = movieRepository.findByLanguage(language,pageRequest);
        }else{
            movies = movieRepository.findAll(pageRequest);
        }

        return Utils.convertPageToPaginationResponse(movies,Utils::convertMovieToMovieDto);
    }
}

