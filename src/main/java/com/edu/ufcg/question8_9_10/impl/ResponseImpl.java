package com.edu.ufcg.question8_9_10.impl;

import com.edu.ufcg.question8_9_10.interfaces.Response;

public class ResponseImpl implements Response {

    private String response;

    public ResponseImpl() {
        this.response = null;
    }

    public synchronized String getResponse() {
        return response;
    }

    public synchronized void setResponse(String newResponse) {
        this.response = newResponse;
    }
}
