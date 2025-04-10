package org.chiches.security;

import org.springframework.stereotype.Component;

@Component
public class TokenHolder {
    private String token;

    public synchronized String getToken() {
        return token;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }
}