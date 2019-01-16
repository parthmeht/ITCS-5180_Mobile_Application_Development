package com.example.group5.moviedatabaseapplication;

import java.io.Serializable;

public class Movie implements Serializable {
    private String name;
    private String description;
    private String movieImdb;
    private Integer movieGenre, movieYear, movieRating;

    Movie(String name, String description, String movieImdb, int movieGenre, int movieYear, int movieRating) {
        this.name = name;
        this.movieYear = movieYear;
        this.description = description;
        this.movieRating = movieRating;
        this.movieGenre = movieGenre;
        this.movieImdb = movieImdb;

    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovieImdb() {
        return movieImdb;
    }

    public void setMovieImdb(String movieImdb) {
        this.movieImdb = movieImdb;
    }

    public Integer getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(Integer movieGenre) {
        this.movieGenre = movieGenre;
    }

    public Integer getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(Integer movieYear) {
        this.movieYear = movieYear;
    }

    public Integer getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(Integer movieRating) {
        this.movieRating = movieRating;
    }
}
