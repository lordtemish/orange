package com.dynamica.orange.Classes;

public class TokenStatus {
    public Object status;
    public String token;
    public TokenStatus(Object status,String token){
        this.status=status;
        this.token=token;
    }

    public Object getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
