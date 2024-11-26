package com.example.acmeplex.moviesystem.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.acmeplex.moviesystem.exceptions.TicketAlreadyCancelledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.example.acmeplex.moviesystem.entity.Showtime;
import com.example.acmeplex.moviesystem.entity.ShowtimeSeat;
import com.example.acmeplex.moviesystem.entity.Ticket;
import com.example.acmeplex.moviesystem.exceptions.DataNotFoundException;
import com.example.acmeplex.moviesystem.exceptions.OperationFailedException;
import com.example.acmeplex.moviesystem.exceptions.TicketCancellationRefusedException;
import com.example.acmeplex.moviesystem.repository.TheatreShowtimeSeatRepository;
import com.example.acmeplex.moviesystem.repository.TicketRepository;


@Service
public class TicketService {

    private final TheatreShowtimeSeatRepository theatreShowtimeSeatRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TheatreShowtimeSeatRepository theatreShowtimeSeatRepository, TicketRepository ticketRepository) {
        this.theatreShowtimeSeatRepository = theatreShowtimeSeatRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Map<String, Object> bookTickets(List<Integer> showtimeSeats, String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            // check if showtime seats are available
            ShowtimeSeat showtimeSeat = theatreShowtimeSeatRepository.findShowtimeSeatById(showtimeSeats.get(0)).orElseThrow(() -> new DataNotFoundException("Showtime Seat"));
            Showtime showtime = theatreShowtimeSeatRepository.findShowtimeById(showtimeSeat.getShowtimeId()).orElseThrow(() -> new DataNotFoundException("Showtime"));
            if (showtimeSeats.size() > showtime.getTickets() - showtime.getTicketsSold()) {
                throw new OperationFailedException("Tickets booking");
            }

            // book tickets
            for(Integer showtimeSeatId : showtimeSeats) {
                int result1 = theatreShowtimeSeatRepository.updateSeatAvailability(showtimeSeatId, false);
                int result2 = ticketRepository.updateTicketReservation(showtimeSeatId, email, Timestamp.valueOf(LocalDateTime.now()));
                if(result1==0 || result2==0) throw new OperationFailedException("Ticket booking");
            }
            String ticketForm = showtimeSeats.size()>1?"tickets":"ticket";
            response.put("success", true);
            response.put("message", String.format("%d %s booked successfully.", showtimeSeats.size(), ticketForm));
            return response;
        } catch (RuntimeException exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.put("error", true);
            response.put("message", exception.getMessage());
            return response;
        }
    }

    @Transactional
    public Map<String, Object> cancelTicketByTicketNumber(String ticketNumber) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Ticket> ticket = ticketRepository.findByTicketNumber(ticketNumber);
            if(ticket.isEmpty()) throw new DataNotFoundException("Ticket");
            if(ticket.get().getHolderEmail() == null) throw new DataNotFoundException("Ticket");

            String status = ticketRepository.findActiveTicketStatus(ticket.get().getTicketNumber(), "refunded");
            if(status==null)
                throw new TicketAlreadyCancelledException();

            int result = ticketRepository.updateTicketStatus(ticket.get().getTicketNumber(), "refunded");
            if (result==0) throw new OperationFailedException("Ticket cancellation");

            //update ticket
            result = ticketRepository.updateTicketReservationByTicketNumber(ticketNumber, null, null);
            if (result==0) throw new OperationFailedException("Ticket cancellation");

            Optional<ShowtimeSeat> showtimeSeat = theatreShowtimeSeatRepository.findShowtimeSeatById(ticket.get().getId());
            if(showtimeSeat.isEmpty()) throw new DataNotFoundException("Showtime Seat");
            Optional<Showtime> showtime  = theatreShowtimeSeatRepository.findShowtimeById(showtimeSeat.get().getShowtimeId());
            if(showtime.isEmpty()) throw new DataNotFoundException("Showtime");

            //check if ticket cancellation is requested within 72 hours before showtime
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.HOUR, 72);
            Timestamp timeAfter = new Timestamp(calendar.getTimeInMillis());
            if(timeAfter.after(showtime.get().getStartTime())) {
                throw new TicketCancellationRefusedException();
            }

            //update seat availability
            result =  theatreShowtimeSeatRepository.updateSeatAvailability(ticket.get().getId(), true);
            if(result==0) throw new OperationFailedException("Set Seat Availability");

            response.put("success", true);
            response.put("message", String.format("Ticket %s cancelled successfully.", ticketNumber));
            return response;
        } catch (RuntimeException exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.put("error", true);
            response.put("message", exception.getMessage());
            return response;
        }
    }
}
