package com.example.training_project.hw5.Q25;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class HW1_SumAndProduct {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int a = 6, b = 7;

        CompletableFuture<Integer> sumFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("[Sum]     running on: " + Thread.currentThread().getName());
            return a + b;
        });

        CompletableFuture<Integer> productFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("[Product] running on: " + Thread.currentThread().getName());
            return a * b;
        });

        CompletableFuture.allOf(sumFuture, productFuture).join();

        System.out.println("Sum     of " + a + " + " + b + " = " + sumFuture.get());
        System.out.println("Product of " + a + " * " + b + " = " + productFuture.get());
    }
}

class HW2_MultiApiMerge {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    // helpers
    public static CompletableFuture<String> getProductInfo() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public static CompletableFuture<String> getReviews() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/comments/1"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public static CompletableFuture<String> getInventory() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    // merge
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> productFuture   = getProductInfo();
        CompletableFuture<String> reviewFuture    = getReviews();
        CompletableFuture<String> inventoryFuture = getInventory();

        // Wait for all 3 to complete, then merge
        String merged = CompletableFuture
                .allOf(productFuture, reviewFuture, inventoryFuture)
                .thenApply(v -> {
                    String product   = productFuture.join();
                    String review    = reviewFuture.join();
                    String inventory = inventoryFuture.join();

                    // In a real app you'd parse JSON; here we show character counts
                    return String.format(
                            "=== Merged API Result ===\n" +
                            "Product   (%d chars): %s\n\n" +
                            "Review    (%d chars): %s\n\n" +
                            "Inventory (%d chars): %s",
                            product.length(),   product.substring(0, Math.min(80, product.length())),
                            review.length(),    review.substring(0, Math.min(80, review.length())),
                            inventory.length(), inventory.substring(0, Math.min(80, inventory.length()))
                    );
                })
                .get();

        System.out.println(merged);
    }
}

class HW3_ExceptionHandling {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Fetch a URL. If anything goes wrong, log it and return defaultValue.
     */
    public static CompletableFuture<String> fetchSafely(String url, String apiName, String defaultValue) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException(
                                apiName + " returned HTTP " + response.statusCode());
                    }
                    return response.body();
                })
                .exceptionally(ex -> {
                    // Log and fall back to default value
                    System.err.println("[WARN] " + apiName + " failed: " + ex.getMessage()
                            + " → using default value");
                    return defaultValue;
                });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> productFuture = fetchSafely(
                "https://jsonplaceholder.typicode.com/posts/1",
                "Product API",
                "{\"id\":0,\"title\":\"Default Product\",\"body\":\"N/A\"}"
        );

        CompletableFuture<String> reviewFuture = fetchSafely(
                "https://jsonplaceholder.typicode.com/comments/1",
                "Review API",
                "{\"id\":0,\"body\":\"No reviews available\"}"
        );

        // Intentionally broken URL → triggers exceptionally()
        CompletableFuture<String> inventoryFuture = fetchSafely(
                "https://jsonplaceholder.typicode.com/INVALID_ENDPOINT_404",
                "Inventory API",
                "{\"stock\":0,\"status\":\"Unknown — API unavailable\"}"
        );

        String result = CompletableFuture
                .allOf(productFuture, reviewFuture, inventoryFuture)
                .thenApply(v -> String.format(
                        "=== Final Merged Result (with fallback) ===\n" +
                        "Product   : %s\n\n" +
                        "Review    : %s\n\n" +
                        "Inventory : %s\n",
                        productFuture.join(),
                        reviewFuture.join(),
                        inventoryFuture.join()
                ))
                .get();

        System.out.println(result);
    }
}

/**
 * Runner for all three homeworks.
 */
public class CompletableFutureHomework {
    public static void main(String[] args) throws Exception {
        System.out.println("========== HW1: Sum & Product ==========");
        HW1_SumAndProduct.main(args);

        System.out.println("\n========== HW2: Multi-API Merge ==========");
        HW2_MultiApiMerge.main(args);

        System.out.println("\n========== HW3: Exception Handling ==========");
        HW3_ExceptionHandling.main(args);
    }
}
