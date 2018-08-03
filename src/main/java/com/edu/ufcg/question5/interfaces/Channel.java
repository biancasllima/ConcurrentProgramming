package com.edu.ufcg.question5.interfaces;

public interface Channel {
    public void putMessage(String message);
    public String takeMessage();
}

