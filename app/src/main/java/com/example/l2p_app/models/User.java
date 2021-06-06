package com.example.l2p_app.models;

public class User {
    public String email;
    public String name;

    public User(){

    }

    public User(String userEmail, String userName) {
        this.email = userEmail;
        this.name = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }
}