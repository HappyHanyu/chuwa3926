package com.example.springioc;

import com.example.springioc.core.MyApplicationContext;
import com.example.springioc.service.UserService;
import com.example.springioc.util.Printer;

public class Main {
    public static void main(String[] args) {
        MyApplicationContext context = new MyApplicationContext("com.example.springioc");

        System.out.println("===== DI Test =====");
        UserService userService = context.getBean(UserService.class);
        userService.test();

        System.out.println("\n===== Singleton Test =====");
        UserService s1 = context.getBean(UserService.class);
        UserService s2 = context.getBean(UserService.class);
        System.out.println("s1 == s2 ? " + (s1 == s2));

        System.out.println("\n===== Prototype Test =====");
        Printer p1 = context.getBean(Printer.class);
        Printer p2 = context.getBean(Printer.class);
        System.out.println("p1 == p2 ? " + (p1 == p2));
        p1.print();
        p2.print();
    }
}