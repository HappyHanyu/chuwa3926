package com.example.training_project.hw5.Q24;

public class PrintNumber1 {

    public static void main(String[] args) throws InterruptedException {

        // Each thread gets its own Runnable with a distinct range
        Thread t0 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }, "Thread-0");

        Thread t1 = new Thread(() -> {
            for (int i = 11; i <= 20; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            for (int i = 21; i <= 30; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }, "Thread-2");

        // Start all threads — order of EXECUTION is non-deterministic
        t0.start();
        t1.start();
        t2.start();

        // Wait for all to finish
        t0.join();
        t1.join();
        t2.join();

        System.out.println("All threads finished.");
    }
}
