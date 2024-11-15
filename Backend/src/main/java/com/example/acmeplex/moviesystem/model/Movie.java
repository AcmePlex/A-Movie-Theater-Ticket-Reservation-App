package com.example.acmeplex.moviesystem.model;

import java.sql.Date;

public class Movie {
    private int id;
    private int genreId;
    private String title;
    private Date releaseDate;
    private String description;
    private String image;
    private int length;

    public Movie() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Movie(int id, int genreId, String title, Date releaseDate, String description, String image, int length) {
        this.id = id;
        this.genreId = genreId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.image = image;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date release_date) {
        this.releaseDate = release_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

