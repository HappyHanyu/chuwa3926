package com.example.training_project.hw5.Q22;

public class SingletonDemo {

    // solution 1：Double-Checked Locking
    static class DoubleCheckedSingleton {
        private static volatile DoubleCheckedSingleton instance;

        private DoubleCheckedSingleton() {}

        public static DoubleCheckedSingleton getInstance() {
            if (instance == null) {
                synchronized (DoubleCheckedSingleton.class) {
                    if (instance == null) {
                        instance = new DoubleCheckedSingleton();
                    }
                }
            }
            return instance;
        }

        public void doSomething() {
            System.out.println("DoubleCheckedSingleton @ " + System.identityHashCode(this));
        }
    }

    // solution 2：Static Inner Class (Recommended Approach: Leverages the Class Loading Mechanism for Lazy Loading and Thread Safety)
    static class HolderSingleton {
        private HolderSingleton() {}

        private static class Holder {
            private static final HolderSingleton INSTANCE = new HolderSingleton();
        }

        public static HolderSingleton getInstance() {
            return Holder.INSTANCE;
        }

        public void doSomething() {
            System.out.println("HolderSingleton @ " + System.identityHashCode(this));
        }
    }

    // Method 3: Enum (Recommended by Joshua Bloch; the most concise approach, and prevents disruption via reflection and deserialization.)
    enum EnumSingleton {
        INSTANCE;

        public void doSomething() {
            System.out.println("EnumSingleton @ " + System.identityHashCode(this));
        }
    }

    // main: Multithreaded Uniqueness Validation
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Double-Checked Locking ===");
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() ->
                    DoubleCheckedSingleton.getInstance().doSomething());
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        System.out.println("\n=== Static Inner Class ===");
        for (int i = 0; i < 5; i++) {
            new Thread(() -> HolderSingleton.getInstance().doSomething()).start();
        }
        Thread.sleep(100);

        System.out.println("\n=== enum ===");
        for (int i = 0; i < 5; i++) {
            new Thread(() -> EnumSingleton.INSTANCE.doSomething()).start();
        }
    }
}
