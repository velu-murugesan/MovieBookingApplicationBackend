package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.MovieRepository;
import com.velu.MovieBookingApplication.dto.MovieDTO;
import com.velu.MovieBookingApplication.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

   public Movie addMovie(MovieDTO movieDTO){
         Movie movie = new Movie();
         movie.setName(movieDTO.getName());
         movie.setDesc(movieDTO.getDesc());
         movie.setGenre(movieDTO.getGenre());
         movie.setRelease_date(movieDTO.getRelease_date());
         movie.setLanguage(movieDTO.getLanguage());
         movie.setDuration(movieDTO.getDuration());

         return movieRepository.save(movie);
   }

    public List<Movie> getAllMovies() {
         return movieRepository.findAll();
    }

    public List<Movie> getAllMoviesByGenre(String genre) {
       Optional<List<Movie>> listOfMoviesBox =  movieRepository.findByGenre(genre);

       if(listOfMoviesBox.isPresent()){
            return listOfMoviesBox.get();
       }
       else throw new RuntimeException("NO Movies are found for this Genre" + " " + genre);
    }

    public List<Movie> getAllMoviesByLanguage(String language) {
      Optional<List<Movie>> listOfMoviesBox =  movieRepository.findByLanguage(language);

      if(listOfMoviesBox.isPresent()){
          return listOfMoviesBox.get();
      }

      else throw new RuntimeException("No Movies are found for this Language" + " " + language);
    }


    public Movie getMovieByTitle(String title) {
        Optional<Movie> movieBox =  movieRepository.findByName(title);

        if(movieBox.isPresent()){
            return movieBox.get();
        }
        throw new RuntimeException("No Movie found for this title" + " " + title);
    }

    public Movie updateMovie(Long id,MovieDTO movieDTO) {

       Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("No Movie Found for the id" + " " + id));

       movie.setName(movieDTO.getName());
       movie.setDesc(movieDTO.getDesc());
       movie.setGenre(movieDTO.getGenre());
       movie.setRelease_date(movieDTO.getRelease_date());
       movie.setDuration(movie.getDuration());
       movie.setLanguage(movie.getLanguage());

      return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
      movieRepository.deleteById(id);
    }
}

