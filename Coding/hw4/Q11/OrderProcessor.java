package com.example.training_project.hw4.Q11;

import java.math.BigDecimal;

public interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        return String.format("$%.2f", price);
    }

    void processOrder(Order order);
}