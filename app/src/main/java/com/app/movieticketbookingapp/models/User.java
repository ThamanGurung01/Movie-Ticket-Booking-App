package com.app.movieticketbookingapp.models;

public class User {
    private String id;
    private String email;
    private String name;
    private String role;

    public User() {}

    public User(String email, String name, String role) {
        this.email = email;
        this.name = name;
    }

    // Getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
