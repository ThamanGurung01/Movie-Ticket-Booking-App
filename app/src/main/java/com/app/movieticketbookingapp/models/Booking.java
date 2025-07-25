package com.app.movieticketbookingapp.models;

import com.google.firebase.Timestamp;

public class Booking {
    private String userEmail;
    private String movieDocId;
    private String movieTitle;
    private int quantity;
    private double pricePerTicket;
    private double totalPrice;
    private Timestamp showTime;
    private Timestamp bookingTime;

    public Booking() {
    }

    public Booking(String userEmail, String movieDocId, String movieTitle,
                   int quantity, double pricePerTicket, double totalPrice,
                   Timestamp showTime, Timestamp bookingTime) {
        this.userEmail = userEmail;
        this.movieDocId = movieDocId;
        this.movieTitle = movieTitle;
        this.quantity = quantity;
        this.pricePerTicket = pricePerTicket;
        this.totalPrice = totalPrice;
        this.showTime = showTime;
        this.bookingTime = bookingTime;
    }

    public String getMovieDocId() {
        return movieDocId;
    }

    public void setMovieDocId(String movieDocId) {
        this.movieDocId = movieDocId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(double pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getShowTime() {
        return showTime;
    }

    public void setShowTime(Timestamp showTime) {
        this.showTime = showTime;
    }

    public Timestamp getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Timestamp bookingTime) {
        this.bookingTime = bookingTime;
    }
}