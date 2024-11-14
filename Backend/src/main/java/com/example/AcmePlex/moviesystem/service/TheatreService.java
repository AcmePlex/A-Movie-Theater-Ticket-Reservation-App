package com.example.AcmePlex.moviesystem.service;

import com.example.AcmePlex.moviesystem.model.Theatre;
import com.example.AcmePlex.moviesystem.repository.TheatreShowtimeSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    private final TheatreShowtimeSeatRepository theatreShowtimeSeatRepository;


    @Autowired
    public TheatreService(TheatreShowtimeSeatRepository theatreShowtimeSeatRepository) {
        this.theatreShowtimeSeatRepository = theatreShowtimeSeatRepository;
    }



    public List<Theatre> getTheatresByMovie(int movieId) {
        return theatreShowtimeSeatRepository.findTheatresByMovie(movieId);
    }
}
