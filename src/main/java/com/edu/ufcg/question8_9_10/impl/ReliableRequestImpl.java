package com.edu.ufcg.question8_9_10.impl;

import com.edu.ufcg.question8_9_10.MainRequest;
import com.edu.ufcg.question8_9_10.interfaces.Request;
import com.edu.ufcg.question8_9_10.interfaces.Response;

import java.util.Scanner;

public class ReliableRequestImpl implements Request {

    private static Response response = new ResponseImpl();;
    private static final String[] mirrors = {"mirror1.com", "mirror2.br", "mirror3.edu"};

    private Thread thread1;
    private Thread thread2;
    private Thread thread3;
    private volatile boolean stop = false;

    private final int MIRROR_1 = 0;
    private final int MIRROR_2 = 1;
    private final int MIRROR_3 = 2;

    private final int SLEEP_3000 = 3000;
    private final int SLEEP_2 = 2;

    public ReliableRequestImpl() {

    }

    @Override
    @Deprecated
    public String request(String serverName) {
        try {
            if (serverName.equals(mirrors[MIRROR_1])) {
                Thread.sleep(SLEEP_3000 * 10);
            }
            else if (serverName.equals(mirrors[MIRROR_2]))
                Thread.sleep(SLEEP_3000);
            else if (serverName.equals(mirrors[MIRROR_3]))
                Thread.sleep(SLEEP_2);
        } catch (InterruptedException e) {
        }

        return serverName;
    }

    @Deprecated
    private synchronized void testAndSet(String valueToSet, Thread threadToFinish1, Thread threadToFinish2) {
        if (response.getResponse() == null) {
            threadToFinish1.interrupt();
            threadToFinish2.interrupt();
            response.setResponse(valueToSet);
        }
    }

    @Override
    @Deprecated
    public String reliableRequest() {

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResponse = request(mirrors[MIRROR_1]);
                testAndSet(rawResponse, thread2, thread3);
            }
        });
        thread1.start();

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResponse = request(mirrors[MIRROR_2]);
                testAndSet(rawResponse, thread1, thread3);
            }
        });
        thread2.start();

        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResponse = request(mirrors[MIRROR_3]);
                testAndSet(rawResponse, thread1, thread2);
            }
        });
        thread3.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
        }
        return response.getResponse();
    }

    @Deprecated
    public String reliableRequestTime() throws Exception {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResponse = request(mirrors[MIRROR_1]);
                testAndSet(rawResponse, thread2, thread3);
            }
        });
        thread1.start();

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResponse = request(mirrors[MIRROR_2]);
                testAndSet(rawResponse, thread1, thread3);
            }
        });
        thread2.start();

        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResponse = request(mirrors[MIRROR_3]);
                testAndSet(rawResponse, thread1, thread2);
            }
        });
        thread3.start();

        try {
            thread1.join(SLEEP_2 * 1000);
        } catch (InterruptedException e) {
        }

        if (response.getResponse() == null) {
            throw new Exception("Requests to mirrors took more than 2 seconds.");
        }

        return response.getResponse();
    }

    @Deprecated
    public void reliableRequestEvent() {
        Thread readStandard = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);

                while (!stop)
                    stop = sc.nextLine().equals("S");
            }
        });

        Thread thread4 = new Thread(new Runnable() {

            public void run() {
                System.out.println("Press S to Stop.");
                while (!stop) {
                    try {
                        System.out.println(reliableRequestTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        stop = true;
                    }
                }
            }

        });

        thread4.start();
        readStandard.start();
    }

    @Deprecated
    public static void main(String[] args) throws InterruptedException {
        MainRequest requestExecutor = new MainRequest();

        try {
            requestExecutor.reliableRequestEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
