package com.app.movieticketbookingapp;

import java.util.List;

public class Movie {
    private String title;
    private int year;
    private String language;
    private int duration;
    private String description;
    private List<String> genres;
    public Movie() {}

    public Movie(String title, int year, String language, int duration, String description, List<String> genres) {
        this.title = title;
        this.year = year;
        this.language = language;
        this.duration = duration;
        this.description = description;
        this.genres = genres;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
