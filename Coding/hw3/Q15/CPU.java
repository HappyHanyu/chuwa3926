package com.example.training_project.hw3.Q15;

public class CPU {
    private String brand;
    private double speed;

    public CPU(String brand, double speed) {
        this.brand = brand;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "CPU{brand='" + brand + "', speed=" + speed + "GHz}";
    }
}
