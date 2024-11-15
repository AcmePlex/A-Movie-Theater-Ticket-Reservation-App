package com.example.acmeplex.moviesystem.model;

import java.sql.Timestamp;

public class Showtime {
    private int id;
    private int movieId;
    private int theatreId;
    private Timestamp startTime;
    private Timestamp endTime;
    private int tickets;
    private int ticketsSold;
    private Timestamp publicAnnouncementTime;

    public Showtime() {
    }

    public Showtime(int id, int movieId, int theatreId, Timestamp startTime, Timestamp endTime,
                    int tickets, int ticketsSold, Timestamp publicAnnouncementTime) {
        this.id = id;
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tickets = tickets;
        this.ticketsSold = ticketsSold;
        this.publicAnnouncementTime = publicAnnouncementTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public Timestamp getPublicAnnouncementTime() {
        return publicAnnouncementTime;
    }

    public void setPublicAnnouncementTime(Timestamp publicAnnouncementTime) {
        this.publicAnnouncementTime = publicAnnouncementTime;
    }
}
