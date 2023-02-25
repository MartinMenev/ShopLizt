package com.example.myshoppingapp.domain.beans;

import com.example.myshoppingapp.domain.enums.UserRole;

public class LoggedUser {
    private Long id;
    private String username;

    private UserRole userRole;

    private String email;

    public LoggedUser() {
    }

    public Long getId() {
        return id;
    }



    public LoggedUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LoggedUser setUsername(String username) {
        this.username = username;
        return this;
    }


    public UserRole getUserRole() {
        return userRole;
    }

    public LoggedUser setUserRole(UserRole userRole) {
        this.userRole = userRole;
        return this;
    }

    public void clearFields() {
        this.id = null;
        this.username = null;
        this.userRole = null;
    }
}
