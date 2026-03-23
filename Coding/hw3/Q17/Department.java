package com.example.training_project.hw3.Q17;

public class Department {
    private String name;
    private String building;

    public Department(String name, String building) {
        this.name = name;
        this.building = building;
    }

    @Override
    public String toString() {
        return "Department{name='" + name + "', building='" + building + "'}";
    }
}
