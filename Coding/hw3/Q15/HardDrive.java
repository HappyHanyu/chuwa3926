package com.example.training_project.hw3.Q15;

public class HardDrive {
    private int size;
    private String type;

    public HardDrive(int size, String type) {
        this.size = size;
        this.type = type;
    }

    @Override
    public String toString() {
        return "HardDrive{size=" + size + "GB, type='" + type + "'}";
    }
}
