package com.example.training_project;

public class Student {
    private String name;
    private int age;
    private double grade;

    public Student(String name, int age, double grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getGrade() { return grade; }

    public void setName(String name) { this.name = name; }

    public void setAge(int age) {
        if (age >= 1 && age <= 150) {
            this.age = age;
        }
    }

    public void setGrade(double grade) {
        if (grade >= 0.0 && grade <= 100.0) {
            this.grade = grade;
        }
    }

    public static void main(String[] args) {
        Student s = new Student("John", 20, 85.5);
        System.out.println(s.getName() + " " + s.getAge() + " " + s.getGrade());

        s.setAge(200); // invalid, should not change
        System.out.println(s.getAge()); // still 20

        s.setGrade(105); // invalid, should not change
        System.out.println(s.getGrade()); // still 85.5
    }
}