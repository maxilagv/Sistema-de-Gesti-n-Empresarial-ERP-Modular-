package com.example.erp.dto;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private long expiresInMillis;

    public AuthResponse() {}
    public AuthResponse(String token, long expiresInMillis) {
        this.token = token;
        this.expiresInMillis = expiresInMillis;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public long getExpiresInMillis() { return expiresInMillis; }
    public void setExpiresInMillis(long expiresInMillis) { this.expiresInMillis = expiresInMillis; }
}

