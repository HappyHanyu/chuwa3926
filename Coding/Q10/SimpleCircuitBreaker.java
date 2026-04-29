package com.example.training_project.hw11.Q10;

public class SimpleCircuitBreaker {
    enum State { CLOSED, OPEN, HALF_OPEN }

    private State state = State.CLOSED;
    private int failureCount = 0;
    private int failureThreshold = 5;
    private long lastFailureTime = 0;
    private long waitDurationMs = 10000; // 10 seconds

    public boolean canExecute() {
        if (state == State.CLOSED) {
            return true;
        }

        if (state == State.OPEN) {
            long now = System.currentTimeMillis();
            if (now - lastFailureTime >= waitDurationMs) {
                state = State.HALF_OPEN;
                return true;
            }
            return false;
        }

        // HALF_OPEN
        return true;
    }

    public void recordSuccess() {
        state = State.CLOSED;
        failureCount = 0;
    }

    public void recordFailure() {
        failureCount++;
        lastFailureTime = System.currentTimeMillis();
        if (state == State.HALF_OPEN || failureCount >= failureThreshold) {
            state = State.OPEN;
        }
    }
}
