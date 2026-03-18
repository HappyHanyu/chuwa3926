package com.example.training_project.hw4.Q11;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private boolean available;

    public Product(String id, String name, BigDecimal price, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }

    @Override
    public String toString() {
        return name + " (" + category + ") - $" + price;
    }
}
