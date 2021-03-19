package com.example.login;

public class AppUser {
    private String firstname;
    private String lastname;
    private String email;

    public AppUser() {
    }

    public AppUser(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }
}
