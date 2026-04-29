package com.example.training_project.hw11.Q9;

public class TokenBucketRateLimiter {
    private final int capacity;
    private final int refillRate; // tokens per second

    private double tokens;
    private long lastRefillTimeNanos;

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        if (capacity <= 0 || refillRate <= 0) {
            throw new IllegalArgumentException("capacity and refillRate must be > 0");
        }
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity; // start full
        this.lastRefillTimeNanos = System.nanoTime();
    }

    public synchronized boolean tryAcquire() {
        long now = System.nanoTime();
        long elapsedNanos = now - lastRefillTimeNanos;

        double tokensToAdd = (elapsedNanos / 1_000_000_000.0) * refillRate;
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimeNanos = now;
        }

        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }
        return false;
    }
}
