package com.example.acmeplex.moviesystem.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import com.example.acmeplex.emailsystem.EmailDetails;
import com.example.acmeplex.emailsystem.EmailService;
import com.example.acmeplex.moviesystem.exceptions.TicketAlreadyCancelledException;
import com.example.acmeplex.paymentsystem.entity.CreditRecord;
import com.example.acmeplex.paymentsystem.repository.CreditRecordRepository;
import com.example.acmeplex.paymentsystem.repository.PaymentRepository;
import com.example.acmeplex.usersystem.service.RegisteredUserService;
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
    private final RegisteredUserService registeredUserService;
    private final CreditRecordRepository creditRecordRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;

    @Autowired
    public TicketService(TheatreShowtimeSeatRepository theatreShowtimeSeatRepository, TicketRepository ticketRepository, RegisteredUserService registeredUserService, CreditRecordRepository creditRecordRepository, PaymentRepository paymentRepository, EmailService emailService) {
        this.theatreShowtimeSeatRepository = theatreShowtimeSeatRepository;
        this.ticketRepository = ticketRepository;
        this.registeredUserService = registeredUserService;
        this.creditRecordRepository = creditRecordRepository;
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
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

            StringBuilder ticketNumberList = new StringBuilder();
            // book tickets
            for(Integer showtimeSeatId : showtimeSeats) {
                String ticketNumber = ticketRepository.getTicketNumber(showtimeSeatId);
                ticketNumberList.append(ticketNumber).append("\n");
                System.out.println(ticketNumber);
                int result1 = theatreShowtimeSeatRepository.updateSeatAvailability(showtimeSeatId, false);
                int result2 = ticketRepository.updateTicketReservation(showtimeSeatId, email, Timestamp.valueOf(LocalDateTime.now()));
                if(result1==0 || result2==0) throw new OperationFailedException("Ticket booking");
            }

            //send email
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(email);
            emailDetails.setSubject("Ticket booking");
            String content = String.format("Dear user,\n" +
                    "Thank you for booking tickets! Here are the ticket numbers: \n" +
                    "%s", ticketNumberList);
            emailDetails.setMsgBody(content);
            int result = emailService.sendSimpleEmail(emailDetails);
            if (result!=0) response.put("emailSent", true);

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

            String status = ticketRepository.findActiveTicketStatus(ticket.get().getTicketNumber(), "paid");
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

            //issue credits
            BigDecimal creditPoints;
            if (registeredUserService.validRegisteredUser(ticket.get().getHolderEmail())){
                creditPoints = ticket.get().getPrice();
            }
            else{
                creditPoints = ticket.get().getPrice().multiply(BigDecimal.valueOf(0.85));
            }

            calendar.setTime(now);
            calendar.add(Calendar.YEAR, 1);
            Date expirationDate = calendar.getTime();

            CreditRecord creditRecord = new CreditRecord(0, ticket.get().getHolderEmail(), creditPoints.doubleValue(), 0, expirationDate);
            creditRecordRepository.addCreditRecord(creditRecord);

            paymentRepository.updatePaymentStatus(ticketNumber, "credited");

            //send email
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(ticket.get().getHolderEmail());
            emailDetails.setSubject("Ticket cancelled");
            String content = String.format("Dear user,\n" +
                    "Your ticket (%s) has been successfully cancelled. You got %.2f credits.", ticketNumber, creditPoints.doubleValue());
            emailDetails.setMsgBody(content);
            result = emailService.sendSimpleEmail(emailDetails);
            if (result!=0) {
                response.put("emailSent", true);
            }

            response.put("success", true);
            response.put("message", String.format("Ticket %s cancelled successfully. You got %.2f credits.", ticketNumber, creditPoints.doubleValue()));
            response.put("credits", creditPoints);
            return response;
        } catch (RuntimeException exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.put("error", true);
            response.put("message", exception.getMessage());
            return response;
        }
    }
}
