package com.edu.ufcg.question4.interfaces;

public interface AppointmentManager {

    public boolean addAppointment (Appointment toAddAppointment);

    public boolean cancel(int appointmentId);
}
