package com.edu.ufcg.question4.impl;


import com.edu.ufcg.question4.interfaces.Appointment;
import com.edu.ufcg.question4.interfaces.AppointmentNotifier;

public class AppointmentNotifierImpl implements AppointmentNotifier {

    public AppointmentNotifierImpl(){}

    public void notify(Appointment appointment) {
        System.out.println(String.format("You have a new appointment %d soon.", appointment.getId()));
    }
}