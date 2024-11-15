package com.example.acmeplex.moviesystem.repository;

import com.example.acmeplex.moviesystem.model.Theatre;
import com.example.acmeplex.moviesystem.model.Showtime;
import com.example.acmeplex.moviesystem.model.dto.ShowtimeSeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TheatreShowtimeSeatRepository {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public TheatreShowtimeSeatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theatre> theatreRowMapper = new RowMapper<Theatre>() {
        @Override
        public Theatre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Theatre(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("rows"),
                    rs.getInt("columns")
            );
        }
    };

    private final RowMapper<Showtime> showtimeRowMapper = new RowMapper<Showtime>() {
        @Override
        public Showtime mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Showtime(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getInt("theatre_id"),
                    rs.getTimestamp("start_time"),
                    rs.getTimestamp("end_time"),
                    rs.getInt("tickets"),
                    rs.getInt("tickets_sold"),
                    rs.getTimestamp("public_announcement_time")
            );
        }
    };

    private final RowMapper<ShowtimeSeatDTO> showtimeSeatDTORowMapper = new RowMapper<ShowtimeSeatDTO>() {
        @Override
        public ShowtimeSeatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ShowtimeSeatDTO(
                    rs.getInt("ss.id"),
                    rs.getInt("row"),
                    rs.getInt("column"),
                    rs.getInt("theatre_row"),
                    rs.getInt("theatre_column"),
                    rs.getBoolean("ss.available")
            );
        }
    };

    public List<Showtime> findShowtimesByMovie(int movie_id)
    {
        String sql = "SELECT * from showtime where movie_id = ?";
        return jdbcTemplate.query(sql, showtimeRowMapper, movie_id);
    }

    public List<Theatre> findTheatresByMovie(int movie_id) {
        String sql = "SELECT * FROM  theatre WHERE id IN (SELECT DISTINCT theatre_id FROM showtime where movie_id = ?)";
        return jdbcTemplate.query(sql, theatreRowMapper, movie_id);
    }

    public List<Showtime> findShowtimesByMovieAndTheatre(int movie_id, int theatre_id)
    {
        String sql = "SELECT * from showtime where movie_id = ? AND theatre_id = ? "
        + "AND start_time BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY) ORDER BY start_time";
        return jdbcTemplate.query(sql, showtimeRowMapper, movie_id, theatre_id);
    }

    public Optional<Theatre> findTheatreById(int movieId) {
        String sql = "SELECT * FROM theatre where id=?";
        List<Theatre> theatres = jdbcTemplate.query(sql, theatreRowMapper, movieId);
        return theatres.isEmpty() ? Optional.empty() : Optional.of(theatres.get(0));
    }

    public List<ShowtimeSeatDTO> findSeatsByShowtime(int showtimeId) {
        String sql = "SELECT ss.id, s.row, s.`column`, s.theatre_row, s.theatre_column, ss.available "
                + "FROM seat AS s INNER JOIN showtime_seat AS ss ON s.id=ss.seat_id WHERE ss.showtime_id = ? ";
        return jdbcTemplate.query(sql, showtimeSeatDTORowMapper, showtimeId);
    }
}
