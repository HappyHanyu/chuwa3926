package com.example.training_project.hw3.Q17;

public class Professor {
    private String name;
    private String specialization;

    public Professor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Professor{name='" + name + "', specialization='" + specialization + "'}";
    }
}
