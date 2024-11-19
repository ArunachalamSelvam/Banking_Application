package com.banking.entities;

import java.util.Date;

public class LoginAttempt {
    private String username;
    private String ipAddress;
    private Date attemptTime;

    public LoginAttempt(String username, String ipAddress, Date attemptTime) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.attemptTime = attemptTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
        this.attemptTime = attemptTime;
    }
}
