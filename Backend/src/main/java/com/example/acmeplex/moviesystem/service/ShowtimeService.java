package com.example.acmeplex.moviesystem.service;

import com.example.acmeplex.moviesystem.model.Showtime;
import com.example.acmeplex.moviesystem.model.Theatre;
import com.example.acmeplex.moviesystem.model.dto.ShowtimeSeatDTO;
import com.example.acmeplex.moviesystem.repository.TheatreShowtimeSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShowtimeService {
    private final TheatreShowtimeSeatRepository theatreShowtimeSeatRepository;

    @Autowired
    public ShowtimeService(TheatreShowtimeSeatRepository theatreShowtimeSeatRepository) {
        this.theatreShowtimeSeatRepository = theatreShowtimeSeatRepository;
    }


    public List<Showtime> getShowtimeList(int movie_id, int theatre_id) {
        return theatreShowtimeSeatRepository.findShowtimesByMovieAndTheatre(movie_id, theatre_id);
    }

    public Map<String, Object> getSeats(int theatre_id, int showtime_id) {
        Map<String, Object> seatsAndTheatreSize = new HashMap<>();
        Optional<Theatre> theatre = theatreShowtimeSeatRepository.findTheatreById(theatre_id);
        List<ShowtimeSeatDTO> seats = theatreShowtimeSeatRepository.findSeatsByShowtime(showtime_id);
        if(theatre.isEmpty()) return null;
        seatsAndTheatreSize.put("theatreRows", theatre.get().getRows());
        seatsAndTheatreSize.put("theatreColumns", theatre.get().getColumns());
        seatsAndTheatreSize.put("seats", seats);
        return seatsAndTheatreSize;
    }
}
