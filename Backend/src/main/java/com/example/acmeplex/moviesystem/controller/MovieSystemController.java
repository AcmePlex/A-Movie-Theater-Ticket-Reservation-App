package com.example.acmeplex.moviesystem.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.acmeplex.moviesystem.dto.MovieNewsDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.acmeplex.moviesystem.dto.MovieDetailedDTO;
import com.example.acmeplex.moviesystem.dto.ShowtimeDTO;
import com.example.acmeplex.moviesystem.dto.TicketBookingDTO;
import com.example.acmeplex.moviesystem.entity.Genre;
import com.example.acmeplex.moviesystem.entity.Theatre;
import com.example.acmeplex.moviesystem.service.MovieService;
import com.example.acmeplex.moviesystem.service.ShowtimeService;
import com.example.acmeplex.moviesystem.service.TheatreService;
import com.example.acmeplex.moviesystem.service.TicketService;

@RestController
public class MovieSystemController {
    private final MovieService movieService;
    private final TheatreService theatreService;
    private final ShowtimeService showtimeService;

    private final TicketService ticketService;

    @Autowired
    public MovieSystemController(MovieService movieService, TheatreService theatreService, ShowtimeService showtimeService, TicketService ticketService) {
        this.movieService = movieService;
        this.theatreService = theatreService;
        this.showtimeService = showtimeService;
        this.ticketService = ticketService;
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getGenres() {
        return ResponseEntity.ok(movieService.getAllGenres());
    }

    @CrossOrigin
    @GetMapping("/movies")
    public ResponseEntity<Map<String, Object>> getMovies(@RequestParam(value = "page", defaultValue = "1")int page,
                                                         @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        return ResponseEntity.ok(movieService.getAllMovies(page, pageSize));
    }

    @GetMapping("/movies/genre/{genre_id}")
    public ResponseEntity<Map<String, Object>> getMoviesByCategory(@PathVariable int genre_id,
                                                                     @RequestParam(value = "page", defaultValue = "1")int page,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre_id, page, pageSize));
    }

    @GetMapping("/movies/search")
    public ResponseEntity<Map<String, Object>> searchMoviesByTitle(@RequestParam("q") String searchQuery,
                                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        System.out.println("search query: " + searchQuery);
        return ResponseEntity.ok(movieService.getMoviesBySearch(searchQuery, page, pageSize));
    }

    @GetMapping("/movies/autocompletion/{searchQuery}")
    public ResponseEntity<List<String>> getSearchAutocompletion(@PathVariable String searchQuery) {
        return ResponseEntity.ok(movieService.getMovieSuggestionByInput(searchQuery));
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<MovieDetailedDTO> getMovieDetailsById(@PathVariable int id) {
        Optional<MovieDetailedDTO> movieDetailedView = movieService.getMovieById(id);
        return ResponseEntity.of(movieDetailedView);
    }

    @GetMapping("/theatres/movie/{movie_id}")
    public ResponseEntity<List<Theatre>> getTheatresByMovie(@PathVariable int movie_id) {
        return ResponseEntity.ok(theatreService.getTheatresByMovie(movie_id));
    }

    @GetMapping("/showtimes/movie/{movie_id}/theatre/{theatre_id}")
    public ResponseEntity<Map<String, List<ShowtimeDTO>>> getShowtimesByMovieAndTheatre(HttpServletRequest request, @PathVariable int movie_id, @PathVariable int theatre_id) {
        //need to find out whether user is logged in
        String header = request.getHeader("Authorization");
        String token = "";
        if (header != null && header.startsWith("Bearer ")) {
            token =  header.substring(7); // Remove "Bearer " prefix
        }
        return ResponseEntity.ok(showtimeService.getShowtimeList(movie_id, theatre_id, !token.isEmpty()));
    }

    @GetMapping("/seats/showroom/{showroomId}/showtime/{showtimeId}")
    public ResponseEntity<Map<String, Object>> getSeats(@PathVariable int showroomId, @PathVariable int showtimeId) {
        Map<String, Object> response = showtimeService.getSeats(showroomId, showtimeId);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel/{ticketNumber}")
    public ResponseEntity<Map<String, Object>> cancelTicket(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(ticketService.cancelTicketByTicketNumber(ticketNumber));
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookTickets(@RequestBody TicketBookingDTO ticketBookingDTO) {
        System.out.println(ticketBookingDTO.getEmail());
        System.out.println(ticketBookingDTO.getIds());
        ResponseEntity<Map<String, Object>> response = ResponseEntity.ok(ticketService.bookTickets(ticketBookingDTO.getIds(), ticketBookingDTO.getEmail()));
        System.out.println(response);
        return response;
    }

    @GetMapping("/movie-news")
    public ResponseEntity<List<MovieNewsDTO>> getMovieNews() {
        System.out.println("movie news requested");
        return ResponseEntity.ok(showtimeService.getMovieNews());
    }
}
