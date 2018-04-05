package com.example.android.medmanagerapplication.user;

public class User {
    public long userId;
    public String email;
    public String password;

    public User(long userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }
}

