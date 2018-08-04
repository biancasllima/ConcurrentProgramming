package com.edu.ufcg.question11;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Middleware {

    enum Status {
        NONEXISTENT, ACQUIRING, READY, RELEASING, FREE
    }

    private final int CORE_POLL_SIZE = 10;
    private final int MAX_POOL_SIZE = 10;
    private final long KEEP_ALIVE_10_MINUTES = 60 * 10;
    private final TimeUnit UNIT = TimeUnit.SECONDS;

    private final int ONE_SECOND = 1000; // ms

    private ThreadPoolExecutor executor;
    private ConcurrentHashMap<Long, Status> virtualMachineStatus;
    private final Object lockAcquire = new Object();
    private Long nextVM = 1L;

    private static Middleware instance = null;

    private Middleware() {
        final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        this.executor = new ThreadPoolExecutor(CORE_POLL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_10_MINUTES, UNIT, workQueue);
        this.virtualMachineStatus = new ConcurrentHashMap<>();
    }

    public static Middleware getInstance() {
        if (instance == null) {
            instance = new Middleware();
        }
        return instance;
    }

    private void dispatchStateChange(Long vm, Status preState, Status postState) {
        virtualMachineStatus.put(vm, preState);

        Runnable workUnit = () -> {
            final long sleepTime = Math.round(Math.random() * 10 * ONE_SECOND);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }

            virtualMachineStatus.put(vm, postState);
        };

        executor.execute(workUnit);
    }

    public void shutDown() {
        executor.shutdown();
    }

    public Long requireVm() {
        Long thisVm;
        synchronized (lockAcquire) {
            thisVm = nextVM;
            nextVM++;

            dispatchStateChange(thisVm, Status.ACQUIRING, Status.READY);
        }
        return thisVm;
    }

    public void releaseVm(Long vm) {
        synchronized (lockAcquire) {
            if (Status.READY.equals(virtualMachineStatus.get(vm))) {
                dispatchStateChange(vm, Status.RELEASING, Status.FREE);
            }
        }
    }

    public Status getStatusVm(Long vm) {
        virtualMachineStatus.putIfAbsent(vm, Status.NONEXISTENT);
        return virtualMachineStatus.get(vm);
    }

    private static final Middleware middleware = Middleware.getInstance();

    public static void main(String[] args) {
        long vm1 = middleware.requireVm();
        long vm2 = middleware.requireVm();
        long vm3 = middleware.requireVm();
        long vm4 = middleware.requireVm();

        while (middleware.getStatusVm(vm1) != Status.FREE ||
                middleware.getStatusVm(vm2) != Status.FREE ||
                middleware.getStatusVm(vm3) != Status.FREE ||
                middleware.getStatusVm(vm4) != Status.FREE) {

            if (middleware.getStatusVm(vm1) == Status.READY) {
                System.out.println(getMessage(vm1));
                middleware.releaseVm(vm1);
            }
            if (middleware.getStatusVm(vm2) == Status.READY) {
                System.out.println(getMessage(vm2));
                middleware.releaseVm(vm2);
            }
            if (middleware.getStatusVm(vm3) == Status.READY) {
                System.out.println(getMessage(vm3));
                middleware.releaseVm(vm3);
            }
            if (middleware.getStatusVm(vm4) == Status.READY) {
                System.out.println(getMessage(vm4));
                middleware.releaseVm(vm4);
            }
        }

        System.out.println("All VM's up and running :).");
        middleware.shutDown();
    }

    private static String getMessage(Long vm) {
        return "(" + vm + ") up and running. Next release now...";
    }

}
