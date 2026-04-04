package com.example.springioc.core;

public interface BeanFactory {
    Object getBean(String beanName);
    <T> T getBean(Class<T> clazz);
}