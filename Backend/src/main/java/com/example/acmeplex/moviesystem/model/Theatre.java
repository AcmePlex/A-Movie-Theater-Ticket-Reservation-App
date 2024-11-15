package com.example.acmeplex.moviesystem.model;

public class Theatre {
    private int id;
    private String name;
    private String location;
    private int rows;
    private int columns;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Theatre(int id, String name, String location, int rows, int columns) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rows = rows;
        this.columns = columns;
    }

    public Theatre() {
    }
}
