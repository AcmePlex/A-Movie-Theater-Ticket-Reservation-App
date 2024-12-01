package com.example.acmeplex.paymentsystem.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.acmeplex.moviesystem.repository.TicketRepository;
import com.example.acmeplex.paymentsystem.dto.CardDTO;
import com.example.acmeplex.paymentsystem.dto.TicketPaymentDTO;
import com.example.acmeplex.paymentsystem.entity.Card;
import com.example.acmeplex.paymentsystem.entity.CreditRecord;
import com.example.acmeplex.paymentsystem.entity.Payment;
import com.example.acmeplex.paymentsystem.repository.CardRepository;
import com.example.acmeplex.paymentsystem.repository.CreditRecordRepository;
import com.example.acmeplex.paymentsystem.repository.PaymentRepository;
import com.example.acmeplex.usersystem.service.RegisteredUserService;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CreditRecordRepository creditRecordRepository;
    private final TicketRepository ticketRepository;
    private final RegisteredUserService registeredUserService;
    private CardRepository cardRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CreditRecordRepository creditRecordRepository, TicketRepository ticketRepository, RegisteredUserService registeredUserService, CardRepository cardRepository) {  
        this.paymentRepository = paymentRepository;
        this.creditRecordRepository = creditRecordRepository;
        this.ticketRepository = ticketRepository;
        this.registeredUserService = registeredUserService;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public String priceCalculation(List<Integer> showtimeSeats){
        try{
        System.out.println("Calculating total price");
        double totalPayment = 0;
        for(Integer showtimeSeatId : showtimeSeats) {
            double price = ticketRepository.getTicketPrice(showtimeSeatId);
            totalPayment += price;
        }
        return String.valueOf(totalPayment);
        } catch (RuntimeException exception) {
        return "error: " + exception.getMessage();
    }
    }

    @Transactional
    public String processMoviePayment(@RequestBody TicketPaymentDTO ticketPaymentDTO){
        try {
            //double totalPayment = priceCalculation(showtimeSeats);
            double totalPayment = ticketPaymentDTO.getTotalPrice();

            double creditUsed=0;
            List<CreditRecord> creditRecords = sortCreditRecords(ticketPaymentDTO.getEmail());
            for(int i=0; i<creditRecords.size(); i++) {
                System.out.println("credit");
                CreditRecord creditRecord = creditRecords.get(i);
                double creditPoints = creditRecord.getCreditPoints();
                double usedPoints = creditRecord.getUsedPoints();
                double creditAvailable = creditPoints - usedPoints;
                if (creditAvailable > 0) {
                    if (creditAvailable >= (totalPayment - creditUsed)) {                        
                        creditRecord.setUsedPoints(usedPoints + totalPayment - creditUsed);
                        creditRecordRepository.updateUsedPoints(creditRecord.getId(), creditRecord.getUsedPoints());
                        creditUsed += creditAvailable;
                    } else {                        
                        creditRecord.setUsedPoints(creditPoints);
                        creditRecordRepository.updateUsedPoints(creditRecord.getId(), creditRecord.getUsedPoints());
                        creditUsed += creditAvailable;
                    }
                }
                if (totalPayment <=creditUsed)  {
                    break;
                }
            }
            System.out.println(creditUsed);
            double remainingPayment = totalPayment - creditUsed;

            int newPaymentId = paymentRepository.getLastPaymentId() + 1;
            Payment payment = new Payment(ticketPaymentDTO.getEmail(), ticketPaymentDTO.getMethod(), newPaymentId, totalPayment, "ticket");
            paymentRepository.addPayment(payment);
            System.out.println("pay2");
            for(Integer showtimeSeatId : ticketPaymentDTO.getIds()) {
                String ticketNumber = ticketRepository.getTicketNumber(showtimeSeatId);
                paymentRepository.addPaymentTicket(payment, ticketNumber , "paid");
            }

           
            return "$"+String.valueOf(totalPayment)+" processed successfully. "+String.valueOf(creditUsed)+" credit points used. "+ticketPaymentDTO.getMethod()+"card has been charged $"+String.valueOf(remainingPayment)+".";         
        } catch (RuntimeException exception) {
            return "error: " + exception.getMessage();
        }
    }

    @Transactional
    public String processMembershipPayment(String email, String method){
        try {
            double totalPayment = 20;
            System.out.println("pay1");
            double creditUsed=0;
            List<CreditRecord> creditRecords = sortCreditRecords(email);
            for(int i=0; i<creditRecords.size(); i++) {
                CreditRecord creditRecord = creditRecords.get(i);
                double creditPoints = creditRecord.getCreditPoints();
                double usedPoints = creditRecord.getUsedPoints();
                double creditAvailable = creditPoints - usedPoints;
                if (creditAvailable > 0) {
                    if (creditAvailable >= (totalPayment - creditUsed)) {
                        creditRecord.setUsedPoints(usedPoints + totalPayment - creditUsed);
                        creditRecordRepository.updateUsedPoints(creditRecord.getId(), creditRecord.getUsedPoints());
                        creditUsed += creditAvailable;
                    } else {
                        creditRecord.setUsedPoints(creditPoints);
                        creditRecordRepository.updateUsedPoints(creditRecord.getId(), creditRecord.getUsedPoints());
                        creditUsed += creditAvailable;
                    }
                }
                if (totalPayment <=creditUsed)  {
                    break;
                }
            }

            double remainingPayment = totalPayment - creditUsed;

            int newPaymentId = paymentRepository.getLastPaymentId() + 1;
            Payment payment = new Payment(email, method, newPaymentId, totalPayment, "membership");
            paymentRepository.addPayment(payment);

            return "$"+String.valueOf(totalPayment)+" processed successfully. "+String.valueOf(creditUsed)+" credit points used. "+method+"card has been charged $"+String.valueOf(remainingPayment)+"."; 
        } catch (RuntimeException exception) {
            return "error: " + exception.getMessage();
        }
    }

    @Transactional
    public String issueCredit(String ticketNumber){
        try {
            System.out.println("Issuing credit points");
            String email = ticketRepository.getEmailByTicketNumber(ticketNumber);

            if (email == null) {
                return "error: Ticket not found";
            }

            double ticketPrice = ticketRepository.getTicketPrice(ticketNumber);
            double creditPoints;
            if (registeredUserService.validRegisteredUser(email)){
                creditPoints = ticketPrice;
            }
            else{
                creditPoints = ticketPrice*0.85;
            }

            int creditId= creditRecordRepository.getLastCreditRecordId() + 1;

            Date today= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.YEAR, 1);
            Date expirationDate = cal.getTime();
            System.out.println("Issuing credit points2");
            CreditRecord creditRecord = new CreditRecord(creditId, email, creditPoints, 0, expirationDate);
            creditRecordRepository.addCreditRecord(creditRecord);

            paymentRepository.updatePaymentStatus(ticketNumber, "credited");

            return "Success! Credit points issued: "+String.valueOf(creditPoints);
        } catch (RuntimeException exception) {
            return "error: " + exception.getMessage();
        }
    }

    private List<CreditRecord> sortCreditRecords(String email) {
        
        List<CreditRecord> creditRecords = creditRecordRepository.getValidCreditRecordByEmail(email);
        Collections.sort(creditRecords, new Comparator<CreditRecord>() {
            @Override
            public int compare(CreditRecord cr1, CreditRecord cr2) {
                return cr1.getExpirationDate().compareTo(cr2.getExpirationDate());
            }
        });
        System.out.println("Credit Records");
        return creditRecords;
    }

    @Transactional
    public Boolean validTicket(String ticketNumber){
        return paymentRepository.getPaymentsByEmail(ticketNumber).isEmpty();
    }
    @Transactional
    public Map<String, Object> addCard(CardDTO cardDTO) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(cardDTO.getCardNumber());
        try {
            Card card = cardDTO.toCard();
            cardRepository.insert(card);
            response.put("success", true);
            response.put("message", "Credit card added successfully");
        } catch (RuntimeException exception) {
            response.put("error", true);
            response.put("message", exception.getMessage());
        }
        return response;
    }
    
}
