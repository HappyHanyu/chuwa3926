package com.example.training_project.hw3.Q14;

public enum OrderStatus implements IStatusCode {
    PENDING(0, "Order is pending"),
    PAID(1, "Payment received"),
    SHIPPED(2, "Order has been shipped"),
    DELIVERED(3, "Order delivered"),
    CANCELLED(-1, "Order cancelled");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() { return code; }

    @Override
    public String getDescription() { return description; }

    public static void main(String[] args) {
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println(status.name()
                    + " | Code: " + status.getCode()
                    + " | " + status.getDescription());
        }

        OrderStatus paid = OrderStatus.valueOf("PAID");
        System.out.println("\nLooked up by name: " + paid.name()
                + " - " + paid.getDescription());
    }
}
