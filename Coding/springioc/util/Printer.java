package com.example.springioc.util;

import com.example.springioc.annotation.MyComponent;
import com.example.springioc.annotation.MyScope;

@MyComponent
@MyScope("prototype")
public class Printer {

    public void print() {
        System.out.println("Printer instance: " + this);
    }
}