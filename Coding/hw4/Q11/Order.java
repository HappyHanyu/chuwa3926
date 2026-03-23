package com.example.training_project.hw4.Q11;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private List<Product> items;
    private String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<Product> getItems() { return items; }
    public String getCustomerEmail() { return customerEmail; }

    @Override
    public String toString() {
        return "Order[" + orderId + "] by " + customerEmail;
    }
}
