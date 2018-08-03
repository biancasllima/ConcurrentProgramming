package com.edu.ufcg.question4.impl;


import com.edu.ufcg.question4.interfaces.Appointment;
import com.edu.ufcg.question4.interfaces.AppointmentManager;
import com.edu.ufcg.question4.interfaces.AppointmentNotifier;

import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppointmentManagerImpl implements AppointmentManager {

    private final static Long MINUTE_TO_MILISECONDS = 60L * 1000;
    private static CopyOnWriteArrayList<Appointment> appointments;
    private static AppointmentNotifier appointmentNotifier;
    private static UtilsImpl utils;

    public AppointmentManagerImpl() {
        AppointmentManagerImpl.appointments = new CopyOnWriteArrayList<Appointment>();
        AppointmentManagerImpl.appointmentNotifier = new AppointmentNotifierImpl();
        AppointmentManagerImpl.utils = new UtilsImpl();
    }

    public boolean addAppointment(Appointment appointment) {
        return AppointmentManagerImpl.appointments.add(appointment);
    }

    public boolean cancel(int appointmentId) {
        for (int i = 0; i < appointments.size(); i++ ) {
            AppointmentImpl storesAppointment = (AppointmentImpl) appointments.get(i);
            if (storesAppointment.getId() == appointmentId) {
                storesAppointment.cancel();
                return appointments.remove(i) != null;
            }
        }
        return false;
    }

    public Runnable notifier = new Runnable() {
        public void run() {
            while (true) {
                for (Appointment appointment : appointments) {
                    Long startTime = utils.milliSecondsUntil(appointment.start());
                    Long timeInSeconds = startTime / 1000;
                    if (timeInSeconds <= 60 && (startTime % (1000 * 15) == 0) && (startTime > 0)) {
                        appointmentNotifier.notify(appointment);
                    }

                    if (appointment.start() == System.currentTimeMillis())
                        System.out.println("Appointment " + appointment.getId() + " starts now.");
                }
            }
        }
    };

    public Runnable canceller = new Runnable() {
        public void run() {
            while (true) {
                Scanner sc = new Scanner(System.in);
                int appointmentId = sc.nextInt();
                cancel(appointmentId);
                System.out.println(AppointmentManagerImpl.appointments);
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        AppointmentManagerImpl appointmentManager = new AppointmentManagerImpl();
        Thread t0 = new Thread(appointmentManager.notifier);
        Thread t1 = new Thread(appointmentManager.canceller);

        t0.start();
        t1.start();

        for (int j = 0; j < 4; j++) {
            String description = String.format("Appointment identifier: %d", j);
            long timestampToStart = System.currentTimeMillis() + MINUTE_TO_MILISECONDS * j;
            long appointmentsDuration = 1000 * 60 * 2L;
            Appointment newAppointment = (Appointment) new AppointmentImpl(j, description, timestampToStart, appointmentsDuration);
            appointmentManager.addAppointment(newAppointment);
        }

    }

}
