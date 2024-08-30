package com.example.tcc.configs;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenStore {

    private String token = "";
    private Long expirationDate = 0L;

    public synchronized String getToken() {
        return token;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

    public synchronized Long getExpirationDate() {
        return expirationDate;
    }

    public synchronized void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public synchronized Boolean isTokenExpired() {
        return expirationDate < new Date(System.currentTimeMillis()).getTime();
    }
}
