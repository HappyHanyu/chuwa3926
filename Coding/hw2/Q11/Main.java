package com.example.training_project.hw2.Q11;

public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
                new Rectangle("red", 4, 5),
                new Circle("blue", 3)
        };

        for (Shape shape : shapes) {
            System.out.printf("Area: %.2f, Perimeter: %.2f%n",
                    shape.getArea(), shape.getPerimeter());
            if (shape instanceof Drawable) {
                ((Drawable) shape).draw();
            }
        }
    }
}
