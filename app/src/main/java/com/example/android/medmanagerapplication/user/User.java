package com.example.android.medmanagerapplication.user;

public class User {
    public long userId;
    public String email;
    public String password;
    public String lastname;
    public String firstname;

    public User(long userId, String email, String password, String firstname, String lastname) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(long userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;

    }
}

