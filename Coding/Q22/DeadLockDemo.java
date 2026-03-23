package com.example.training_project.hw5.Q22;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockDemo {

    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    static void deadlockDemo() {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("T1 has A，wait B");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (LOCK_B) {   // T2 already have B
                    System.out.println("T1 got B");
                }
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("T2 got B，wait A");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (LOCK_A) {
                    System.out.println("T2 got A");
                }
            }
        }, "T2");

        t1.start();
        t2.start();
        // deadlock
    }

    // Solution 1: Standardize the Locking Order (Simplest and Most Common)—Breaking the "Circular Wait" Condition
    static void fixByOrderedLocking() throws InterruptedException {
        System.out.println("=== Solution 1 ===");

        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (LOCK_B) {
                    System.out.println("T1 completed");
                }
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_A) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (LOCK_B) {
                    System.out.println("T2 completed");
                }
            }
        }, "T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("T1, T2 completed");
    }

    // Solution 2: tryLock with Timeout (Using ReentrantLock to break the "Hold and Wait" condition)
    static void fixByTryLock() throws InterruptedException {
        System.out.println("\n=== Solution 2 ===");
        final ReentrantLock lockA = new ReentrantLock();
        final ReentrantLock lockB = new ReentrantLock();

        Runnable task = () -> {
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    if (lockA.tryLock(100, TimeUnit.MILLISECONDS)) {
                        try {
                            if (lockB.tryLock(100, TimeUnit.MILLISECONDS)) {
                                try {
                                    System.out.println(Thread.currentThread().getName() + " 完成");
                                    return;
                                } finally {
                                    lockB.unlock();
                                }
                            }
                        } finally {
                            lockA.unlock();
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");
        t1.start(); t2.start();
        t1.join();  t2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        // deadlockDemo();
        fixByOrderedLocking();
        fixByTryLock();
    }
}
