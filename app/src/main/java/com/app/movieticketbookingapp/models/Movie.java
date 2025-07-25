package com.app.movieticketbookingapp.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class Movie {
    private String id;
    private String title;
    private int year;
    private String language;
    private int duration;
    private String description;
    private List<String> genres;
    private Timestamp createdAt;
    private Timestamp showTime;
    private int totalTickets;
    private double ticketPrice;
    public Movie() {}

    public Movie(String title, int year, String language, int duration, String description, List<String> genres,Timestamp createdAt, Timestamp showTime, int totalTickets,double ticketPrice) {
        this.title = title;
        this.year = year;
        this.language = language;
        this.duration = duration;
        this.description = description;
        this.genres = genres;
        this.createdAt = createdAt;
        this.showTime=showTime;
        this.totalTickets=totalTickets;
        this.ticketPrice=ticketPrice;
    }
    // Getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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

    public double getTicketPrice(){return ticketPrice;}
    public void setTicketPrice(double ticketPrice){this.ticketPrice=ticketPrice;}
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public Timestamp getShowTime() {
        return showTime;
    }

    public void setShowTime(Timestamp showTime) {
        this.showTime = showTime;
    }
    public int getTotalTickets(){
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
}
