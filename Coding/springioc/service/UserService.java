package com.example.springioc.service;

import com.example.springioc.annotation.MyAutowired;
import com.example.springioc.annotation.MyComponent;
import com.example.springioc.util.Printer;

@MyComponent
public class UserService {

    @MyAutowired
    private UserRepository userRepository;

    @MyAutowired
    private Printer printer;

    public void test() {
        System.out.println("UserService instance: " + this);
        System.out.println("Injected userRepository says: " + userRepository.getUserName());
        printer.print();
    }
}