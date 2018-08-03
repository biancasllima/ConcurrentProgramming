package com.edu.ufcg.question4.impl;

import com.edu.ufcg.question4.interfaces.Appointment;

public class AppointmentImpl implements Appointment {

    private int id;
    private String description;
    private long start;
    private long duration;
    private boolean active;

    public AppointmentImpl(int id, String description, long start, long duration) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.duration = duration;
        this.active = true;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public long start() {
        return this.start;
    }

    public long duration() {
        return this.duration;
    }

    public boolean isActive() {
        return this.active;
    }

    public void activate() {
        this.active = true;
    }

    public void cancel() {
        this.active = false;
    }
}