package com.example.springioc.service;

import com.example.springioc.annotation.MyComponent;

@MyComponent
public class UserRepository {

    public String getUserName() {
        return "Alice";
    }
}