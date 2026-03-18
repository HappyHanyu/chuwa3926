package com.example.training_project.hw4.Q11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {
        System.out.println("Processing " + order
                + " | Total: " + OrderProcessor.formatPrice(calculateTotal(order)));
    }

    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        return orders.stream()
                .filter(order -> !order.getItems().isEmpty())
                .collect(Collectors.groupingBy(
                        order -> order.getItems().get(0).getCategory()
                ));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }

    public static void main(String[] args) {
        OrderService service = new OrderService();

        // Create products
        Product p1 = new Product("1", "Laptop",    new BigDecimal("999.99"), "Electronics", true);
        Product p2 = new Product("2", "Mouse",     new BigDecimal("29.99"),  "Electronics", true);
        Product p3 = new Product("3", "Desk",      new BigDecimal("249.99"), "Furniture",   true);
        Product p4 = new Product("4", "Chair",     new BigDecimal("199.99"), "Furniture",   true);
        Product p5 = new Product("5", "Notebook",  new BigDecimal("5.99"),   "Stationery",  true);

        // Create orders
        Order o1 = new Order("O1", LocalDateTime.now(), Arrays.asList(p1, p2), "alice@example.com");
        Order o2 = new Order("O2", LocalDateTime.now(), Arrays.asList(p3, p4), "bob@example.com");
        Order o3 = new Order("O3", LocalDateTime.now(), Arrays.asList(p5),     "carol@example.com");
        Order o4 = new Order("O4", LocalDateTime.now(), Arrays.asList(p2, p5), "dave@example.com");

        List<Order> allOrders = Arrays.asList(o1, o2, o3, o4);

        // Process all orders
        System.out.println("=== Processing All Orders ===");
        allOrders.forEach(service::processOrder);

        // Filter orders with total > $100 using lambda
        System.out.println("\n=== Orders with Total > $100 ===");
        List<Order> expensiveOrders = service.filterOrders(
                allOrders,
                order -> service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0
        );
        expensiveOrders.forEach(order ->
                System.out.println(order + " | Total: "
                        + OrderProcessor.formatPrice(service.calculateTotal(order)))
        );

        // Group orders by category
        System.out.println("\n=== Orders Grouped by Category ===");
        Map<String, List<Order>> grouped = service.groupOrdersByCategory(allOrders);
        grouped.forEach((category, orders) -> {
            System.out.println(category + ":");
            orders.forEach(order -> System.out.println("  " + order));
        });

        // Find most expensive order and handle Optional
        System.out.println("\n=== Most Expensive Order ===");
        service.findMostExpensiveOrder(allOrders)
                .ifPresent(order -> System.out.println(order
                        + " | Total: " + OrderProcessor.formatPrice(service.calculateTotal(order))));
    }
}
