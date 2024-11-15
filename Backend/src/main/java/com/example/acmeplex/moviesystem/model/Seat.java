package com.example.acmeplex.moviesystem.model;

public class Seat {
    private int id;
    private int theatreId;
    private int row;
    private int column;
    private int theatreRow;
    private int theatreColumn;

    public Seat() {
    }

    public Seat(int id, int theatreId, int row, int column, int theatreRow, int theatreColumn) {
        this.id = id;
        this.theatreId = theatreId;
        this.row = row;
        this.column = column;
        this.theatreRow = theatreRow;
        this.theatreColumn = theatreColumn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getTheatreRow() {
        return theatreRow;
    }

    public void setTheatreRow(int theatreRow) {
        this.theatreRow = theatreRow;
    }

    public int getTheatreColumn() {
        return theatreColumn;
    }

    public void setTheatreColumn(int theatreColumn) {
        this.theatreColumn = theatreColumn;
    }
}

