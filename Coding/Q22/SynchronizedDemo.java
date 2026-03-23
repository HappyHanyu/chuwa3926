package com.example.training_project.hw5.Q22;

public class SynchronizedDemo {

    private int instanceCount = 0;
    private static int classCount = 0;

    // 1: Synchronized instance method: locks `this`.
    public synchronized void incrementInstance() {
        instanceCount++;
        System.out.println(Thread.currentThread().getName()
                + " [instance method] instanceCount = " + instanceCount);
    }

    // 2: synchronized block, locking `this`
    public void incrementInstanceBlock() {
        System.out.println(Thread.currentThread().getName() + " Access Method (Unlocked Section)");
        synchronized (this) {
            instanceCount++;
            System.out.println(Thread.currentThread().getName()
                    + " [Synchronization Block] instanceCount = " + instanceCount);
        }
        System.out.println(Thread.currentThread().getName() + " Exit Method (Unlocked Section)");
    }

    // 3：static synchronized
    public static synchronized void incrementClass() {
        classCount++;
        System.out.println(Thread.currentThread().getName()
                + " [Static method] classCount = " + classCount);
    }

    // Instance locks for different instances are not mutually exclusive.
    public static void main(String[] args) throws InterruptedException {

        SynchronizedDemo obj1 = new SynchronizedDemo();
        SynchronizedDemo obj2 = new SynchronizedDemo();

        System.out.println("=== For a single instance, instance methods are mutually exclusive. ===");
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) obj1.incrementInstance();
        }, "T1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) obj1.incrementInstance();
        }, "T2");
        t1.start(); t2.start();
        t1.join();  t2.join();
        System.out.println("finally instanceCount(obj1) = " + obj1.instanceCount); // must 6

        System.out.println("\n=== For different instances, instance locks are not mutually exclusive. ===");
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 3; i++) obj1.incrementInstance();
        }, "T3-obj1");
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 3; i++) obj2.incrementInstance();
        }, "T4-obj2");
        t3.start(); t4.start();
        t3.join();  t4.join();

        System.out.println("\n=== static synchronized，All instances share the class lock. ===");
        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 3; i++) SynchronizedDemo.incrementClass();
        }, "T5");
        Thread t6 = new Thread(() -> {
            for (int i = 0; i < 3; i++) SynchronizedDemo.incrementClass();
        }, "T6");
        t5.start(); t6.start();
        t5.join();  t6.join();
        System.out.println("finally classCount = " + classCount); // must 6
    }
}
