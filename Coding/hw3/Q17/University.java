package com.example.training_project.hw3.Q17;

import java.util.ArrayList;
import java.util.List;

public class University {
    private String name;
    private List<Department> departments = new ArrayList<>();
    private List<Professor> professors = new ArrayList<>();

    public University(String name) {
        this.name = name;
        departments.add(new Department("Computer Science", "Building A"));
        departments.add(new Department("Mathematics", "Building B"));
        departments.add(new Department("Biology", "Building C"));
    }

    public void addProfessor(Professor p) {
        professors.add(p);
    }

    public void listProfessors() {
        System.out.println("Professors at " + name + ":");
        professors.forEach(p -> System.out.println("  " + p));
    }

    public static void main(String[] args) {
        Professor p1 = new Professor("Dr. Smith", "AI");
        Professor p2 = new Professor("Dr. Lee", "Calculus");

        University university = new University("Tech University");
        university.addProfessor(p1);
        university.addProfessor(p2);
        university.listProfessors();

        university = null;
        System.out.println("\nAfter university set to null:");
        System.out.println(p1);
        System.out.println(p2);
    }
}
