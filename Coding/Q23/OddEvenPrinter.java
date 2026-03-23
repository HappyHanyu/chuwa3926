package com.example.training_project.hw5.Q23;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrinter {

    // Solution 1: synchronized + wait / notify
    static class SolutionWaitNotify {
        private static final Object monitor = new Object();
        private static int value = 1;

        static class PrintRunnable implements Runnable {
            @Override
            public void run() {
                synchronized (monitor) {
                    while (value <= 10) {
                        System.out.println(Thread.currentThread().getName() + ": " + value++);
                        monitor.notifyAll();           // wake up the other thread
                        try {
                            if (value <= 10) {
                                monitor.wait();        // release lock, wait for our turn
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    monitor.notifyAll();               // unblock the other thread so it can exit
                }
            }
        }

        static void run() throws InterruptedException {
            System.out.println("=== Solution 1: synchronized + wait/notify ===");
            value = 1;
            PrintRunnable runnable = new PrintRunnable(); // SAME runnable → shared monitor
            Thread t0 = new Thread(runnable, "OddThread");
            Thread t1 = new Thread(runnable, "EvenThread");
            t0.start();
            t1.start();
            t0.join();
            t1.join();
        }
    }

    // Solution 2: ReentrantLock + await / signal  (matches repo style)
    static class SolutionReentrantLock {
        private static int value = 1;

        static class PrintRunnable implements Runnable {
            private final Lock lock = new ReentrantLock();
            private final Condition condition = lock.newCondition();

            @Override
            public void run() {
                lock.lock();
                try {
                    while (value <= 10) {
                        System.out.println(Thread.currentThread().getName() + ": " + value++);
                        condition.signalAll();         // wake up the waiting thread
                        try {
                            if (value <= 10) {
                                condition.await();     // release lock, wait for signal
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        static void run() throws InterruptedException {
            System.out.println("\n=== Solution 2: ReentrantLock + await/signal ===");
            value = 1;
            PrintRunnable runnable = new PrintRunnable(); // SAME runnable → shared lock & condition
            Thread t0 = new Thread(runnable, "OddThread");
            Thread t1 = new Thread(runnable, "EvenThread");
            t0.start();
            t1.start();
            t0.join();
            t1.join();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SolutionWaitNotify.run();
        Thread.sleep(200);
        SolutionReentrantLock.run();
    }
}
