package com.example.training_project.hw5.Q22;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ReentrantLockConditionDemo {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition(); // Producers have been waiting for this.
    private final Condition notEmpty = lock.newCondition(); // Consumers have been waiting for this.

    private final Queue<Integer> queue = new LinkedList<>();
    private static final int MAX_SIZE = 5;

    // Producers
    public void produce(int item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() >= MAX_SIZE) {
                System.out.println("[Producer] queue full，await...");
                notFull.await();
            }
            queue.add(item);
            System.out.println("[Producer] Production: " + item + "，queue size: " + queue.size());
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Consumers
    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println("[Consumer] queue empty，await...");
                notEmpty.await();
            }
            int item = queue.poll();
            System.out.println("[Consumer] Consumption: " + item + "，queue size: " + queue.size());
            notFull.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    // main
    public static void main(String[] args) {
        ReentrantLockConditionDemo demo = new ReentrantLockConditionDemo();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                try {
                    demo.produce(i);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 8; i++) {
                try {
                    demo.consume();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();
    }
}
