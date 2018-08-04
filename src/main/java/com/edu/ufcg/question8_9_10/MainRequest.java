package com.edu.ufcg.question8_9_10;


import com.edu.ufcg.question8_9_10.interfaces.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainRequest implements Request {

    private final int TOTAL_THREADS = 3;
    private ExecutorService executor = Executors.newFixedThreadPool(TOTAL_THREADS);

    private volatile boolean stop = false;

    private static final String[] mirrors = {"mirror1.com", "mirror2.br", "mirror3.edu"};
    private final int MIRROR_1 = 0;
    private final int MIRROR_2 = 1;
    private final int MIRROR_3 = 2;
    private final int SLEEP_10000 = 30000;
    private final int SLEEP_2000 = 2000;

    public MainRequest() {
    }

    public String reliableRequest() {
        List<Callable<String>> tasks = createRequests();

        try {
            String result = executor.invokeAny(tasks);
            executor.shutdown();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String request(String serverName) {
        try {
            if (serverName.equals(mirrors[MIRROR_1])) {
                Thread.sleep(SLEEP_10000);
            } else if (serverName.equals(mirrors[MIRROR_2])) {
                Thread.sleep(SLEEP_2000);
            } else if (serverName.equals(mirrors[MIRROR_3])) {
                Thread.sleep(SLEEP_2000);
            }
        } catch (InterruptedException e) {
        }

        return serverName;
    }

    private List<Callable<String>> createRequests() {
        Callable<String> request1 = () -> {
            return request(mirrors[MIRROR_1]);
        };
        Callable<String> request2 = () -> {
            return request(mirrors[MIRROR_2]);
        };
        Callable<String> request3 = () -> {
            return request(mirrors[MIRROR_3]);
        };

        List<Callable<String>> tasks = new ArrayList<>();
        tasks.add(request1);
        tasks.add(request2);
        tasks.add(request3);
        return tasks;
    }

    public String reliableRequestTime() throws Exception {
        List<Callable<String>> requests = createRequests();

        try {
            String result = executor.invokeAny(requests, SLEEP_2000, TimeUnit.MILLISECONDS);
            executor.shutdown();
            return result;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            executor.shutdown();
            e.printStackTrace();
        }
        return null;
    }

    public void reliableRequestEvent() {
        Thread guardThread = new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(System.in);
                while (!stop)
                    stop = sc.nextLine().contains("s");

            }
        });

        Thread executingThead = new Thread(new Runnable() {
            public void run() {
                System.out.println("Press S to stop at any moment.");
                while (!stop) {
                    try {
                        System.out.println(reliableRequestTime());
                        executor = Executors.newFixedThreadPool(TOTAL_THREADS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        executingThead.start();
        guardThread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        MainRequest request = new MainRequest();
        try {
            System.out.println("Mirror used: " + request.reliableRequestTime());
            //System.out.println("Mirror used: " + request.reliableRequest());
            request.reliableRequestEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
