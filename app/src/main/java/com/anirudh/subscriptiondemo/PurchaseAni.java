package com.anirudh.subscriptiondemo;

import java.io.Serializable;

public class PurchaseAni implements Serializable {

    private String name;
    private String token;
    private long expiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }
}
