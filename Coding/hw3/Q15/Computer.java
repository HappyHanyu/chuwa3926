package com.example.training_project.hw3.Q15;

public class Computer {
    private CPU cpu;
    private RAM ram;
    private HardDrive hardDrive;

    public Computer(String cpuBrand, double cpuSpeed, int ramSize, int hdSize, String hdType) {
        this.cpu = new CPU(cpuBrand, cpuSpeed);
        this.ram = new RAM(ramSize);
        this.hardDrive = new HardDrive(hdSize, hdType);
    }

    public String getSpecs() {
        return "Computer Specs:\n  " + cpu + "\n  " + ram + "\n  " + hardDrive;
    }

    public static void main(String[] args) {
        Computer computer = new Computer("Intel", 3.5, 16, 512, "SSD");
        System.out.println(computer.getSpecs());
    }
}
