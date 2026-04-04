package com.example.springioc.core;

import com.example.springioc.annotation.MyAutowired;
import com.example.springioc.annotation.MyComponent;
import com.example.springioc.annotation.MyScope;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MyApplicationContext implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<String, Object> singletonObjects = new HashMap<>();

    public MyApplicationContext(String basePackage) {
        scan(basePackage);
        initSingletonBeans();
    }

    private void scan(String basePackage) {
        String path = basePackage.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            throw new RuntimeException("Package not found: " + basePackage);
        }

        File dir = new File(resource.getFile());
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("Invalid package path: " + dir.getAbsolutePath());
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scan(basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        String beanName = generateBeanName(clazz);
                        String scope = "singleton";

                        if (clazz.isAnnotationPresent(MyScope.class)) {
                            scope = clazz.getAnnotation(MyScope.class).value();
                        }

                        BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz, scope);
                        beanDefinitionMap.put(beanName, beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Failed to load class: " + className, e);
                }
            }
        }
    }

    private void initSingletonBeans() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanDefinition);
                singletonObjects.put(entry.getKey(), bean);
            }
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        try {
            Class<?> clazz = beanDefinition.getBeanClass();
            Object instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(MyAutowired.class)) {
                    Object dependency = getBean(field.getType());
                    field.setAccessible(true);
                    field.set(instance, dependency);
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + beanDefinition.getBeanName(), e);
        }
    }

    private String generateBeanName(Class<?> clazz) {
        MyComponent component = clazz.getAnnotation(MyComponent.class);
        if (component != null && !component.value().trim().isEmpty()) {
            return component.value();
        }

        String simpleName = clazz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("No bean named: " + beanName);
        }

        if ("singleton".equals(beanDefinition.getScope())) {
            return singletonObjects.get(beanName);
        }

        if ("prototype".equals(beanDefinition.getScope())) {
            return createBean(beanDefinition);
        }

        throw new RuntimeException("Unsupported scope: " + beanDefinition.getScope());
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            if (clazz.isAssignableFrom(entry.getValue().getBeanClass())) {
                return clazz.cast(getBean(entry.getKey()));
            }
        }
        throw new RuntimeException("No bean found for type: " + clazz.getName());
    }
}