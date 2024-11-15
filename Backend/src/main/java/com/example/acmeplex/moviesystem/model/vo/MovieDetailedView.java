package com.example.acmeplex.moviesystem.model.vo;

import com.example.acmeplex.moviesystem.model.dto.MovieGenreDTO;

import java.sql.Date;

public class MovieDetailedView {
    private int id;
    private String genre;
    private String title;
    private Date releaseDate;
    private String description;
    private String image;
    private int length;
    private boolean hasShowtime;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public MovieDetailedView(MovieGenreDTO movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.description = movie.getDescription();
        this.image = movie.getImage();
        this.length = movie.getLength();
        this.hasShowtime = false;
        this.genre = movie.getGenre();
    }

    public MovieDetailedView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public boolean isHasShowtime() {
        return hasShowtime;
    }

    public void setHasShowtime(boolean hasShowtime) {
        this.hasShowtime = hasShowtime;
    }
}
