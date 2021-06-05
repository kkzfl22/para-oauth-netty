package com.paraview.oauth.bean;

import java.io.Serializable;
import java.util.UUID;

public class Token implements Serializable {

    private static final long serialVersionUID = 2L;

    private String access_token;

    private String token_type;

    private long expires_in;

    private long timestap;

    public Token() {
    }

    public Token(String token_type, long expires_in) {
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.timestap = System.currentTimeMillis();
        this.access_token = UUID.randomUUID().toString();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public long getTimestap() {
        return timestap;
    }

    public void setTimestap(long timestap) {
        this.timestap = timestap;
    }

    @Override
    public String toString() {
        return "{" +
                "\"access_token\":\"" + access_token + "\"" +
                ", \"token_type\":\"" + token_type + "\"" +
                ", \"expires_in\":" + expires_in +
                ", \"timestap\":" + timestap +
                "}";
    }

}
