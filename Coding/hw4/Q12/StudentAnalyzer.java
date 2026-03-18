package com.example.training_project.hw4.Q12;

import java.util.*;
import java.util.stream.*;

public class StudentAnalyzer {

    // Top n students by average score, sorted descending
    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    // Average score per major
    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.averagingDouble(Student::getAverageScore)
                ));
    }

    // Student with the highest single score across all scores
    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(
                        s -> s.getScores().stream()
                                .mapToDouble(Double::doubleValue)
                                .max()
                                .orElse(0.0)
                ));
    }

    // Students whose average is above their major's average
    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        double majorAvg = students.stream()
                .filter(s -> s.getMajor().equals(major))
                .mapToDouble(Student::getAverageScore)
                .average()
                .orElse(0.0);

        return students.stream()
                .filter(s -> s.getMajor().equals(major))
                .filter(s -> s.getAverageScore() > majorAvg)
                .collect(Collectors.toList());
    }

    // Partition students into pass/fail by average score
    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getAverageScore() >= passingScore
                ));
    }

    public static void main(String[] args) {
        StudentAnalyzer analyzer = new StudentAnalyzer();

        // Create at least 6 students with different majors and scores
        List<Student> students = Arrays.asList(
                new Student("S1", "Alice",   21, "CS",   Arrays.asList(90.0, 85.0, 92.0)),
                new Student("S2", "Bob",     22, "CS",   Arrays.asList(70.0, 65.0, 75.0)),
                new Student("S3", "Carol",   20, "Math", Arrays.asList(95.0, 98.0, 91.0)),
                new Student("S4", "Dave",    23, "Math", Arrays.asList(60.0, 72.0, 68.0)),
                new Student("S5", "Eve",     21, "CS",   Arrays.asList(88.0, 91.0, 84.0)),
                new Student("S6", "Frank",   22, "Bio",  Arrays.asList(77.0, 80.0, 74.0))
        );

        // Top 3 students by average score
        System.out.println("=== Top 3 Students ===");
        analyzer.getTopStudentNames(students, 3)
                .forEach(System.out::println);

        // Average score by major
        System.out.println("\n=== Average Score by Major ===");
        analyzer.getAverageScoreByMajor(students)
                .forEach((major, avg) ->
                        System.out.printf("%s: %.2f%n", major, avg));

        // Student with highest single score
        System.out.println("\n=== Student with Highest Single Score ===");
        analyzer.findStudentWithHighestSingleScore(students)
                .ifPresent(s -> System.out.println(s.getName()
                        + " - highest score: "
                        + s.getScores().stream().mapToDouble(Double::doubleValue).max().orElse(0.0)));

        // CS students above their major average
        System.out.println("\n=== CS Students Above Major Average ===");
        analyzer.getStudentsAboveAverageInMajor(students, "CS")
                .forEach(System.out::println);

        // Pass/fail partition with passing score 75.0
        System.out.println("\n=== Pass / Fail (passing score: 75.0) ===");
        Map<Boolean, List<Student>> partition = analyzer.partitionByPassFail(students, 75.0);
        System.out.println("Passed: " + partition.get(true));
        System.out.println("Failed: " + partition.get(false));
    }
}
