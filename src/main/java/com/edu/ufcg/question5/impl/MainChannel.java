package com.edu.ufcg.question5.impl;

import com.edu.ufcg.question5.interfaces.Channel;

public class MainChannel {

    private final static int BUFFER_SIZE = 5;
    private static Channel channel = new ChannelImpl(BUFFER_SIZE);

    private static Runnable thread1 = new Runnable() {
        public void run() {
            try {
                for (int i = 0; i < BUFFER_SIZE; i++) {
                    System.out.println("Thread 1, position: " + i);
                    channel.putMessage("Thread 1 message: " + i);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    };

    private static Runnable thread2 = new Runnable() {
        public void run() {
            try {
                for (int j = 0; j < BUFFER_SIZE; j++) {
                    String message = channel.takeMessage();
                    System.out.println("Take in thread 2, message: " + message);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    };

    public static void main(String[] args) {
        Thread t01 = new Thread(thread1);
        Thread t02 = new Thread(thread2);

        t01.start();
        t02.start();

        try {
            t01.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t02.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
