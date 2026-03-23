Q1. 
A Functional Interface is an interface that has exactly one abstract method. The annotation used to mark it is @FunctionalInterface, but this annotation is not mandatory. It is just used to tell the compiler to verify that the interface has only one abstract method. If you accidentally add a second abstract method, the compiler will give you an error.
Two built-in examples in Java 8 are Predicate<T> which has the abstract method boolean test(T t) used to test a condition and return true or false, and Function<T, R> which has the abstract method R apply(T t) used to take an input of type T and return a result of type R.

Q2. 
Hello from Greeting
And hello from Person

Q3. 
The conversion steps are: first remove the new Comparator<String>() and the @Override annotation. Then remove the method name compare and the return type. Then remove the parameter types since Java can infer them. Finally since the body has only one statement, remove the curly braces and the return keyword.

Q4.
A is valid. Runnable has one abstract method run() with no parameters and no return value, so () -> System.out.println("Running") is correct.
B is invalid. You cannot use the return keyword without curly braces. It should be either s -> s.isEmpty() or s -> { return s.isEmpty(); }.
C is invalid. The body has curly braces but no return statement. It should be x -> { return x * 2; }.
D is valid. You can explicitly specify the parameter type inside a lambda expression.
E is valid. BiFunction takes two Integer parameters and returns an Integer, so (a, b) -> a + b is correct.
F is valid. When using curly braces, you need an explicit return statement, and this one has it correctly.

Q5. 
`x -> System.out.println(x)` matches `System.out::println`. This is a **Bound Instance** method reference because `out` is a specific, already-known instance of `PrintStream`.

`s -> s.toUpperCase()` matches `String::toUpperCase`. This is an **Unbound Instance** method reference because `toUpperCase()` is called on the parameter `s` itself, and the instance is not known until runtime.

`x -> Math.abs(x)` matches `Math::abs`. This is a **Static** method reference because `Math.abs` is a static method on the `Math` class.

`() -> new ArrayList<>()` matches `ArrayList::new`. This is a **Constructor** reference.

`(s1, s2) -> s1.compareTo(s2)` matches `String::compareTo`. This is an **Unbound Instance** method reference because `compareTo` is called on the first parameter `s1`, with `s2` as the argument.

Q6. 
`Optional.of(value)` assumes that the value is never null, and it will immediately throw a `NullPointerException` if you pass a null value into it. `Optional.ofNullable(value)` on the other hand safely handles null by wrapping it into an empty Optional instead of throwing an exception.
In the given code, `value` is null. When `Optional.of(value)` is executed, it throws a `NullPointerException` immediately at that line, so the rest of the code never runs. If `opt1` were not there, `opt2 = Optional.ofNullable(value)` would succeed and create an empty Optional, and `opt2.isPresent()` would print `false`.

Q7. 
The output will be:
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
The key difference is about when the fallback value is evaluated. With orElse(createDefault()), the method createDefault() is called eagerly as an argument before orElse even checks whether the Optional is empty. This means "Creating default value" is always printed regardless of whether the Optional has a value or not. With orElseGet(() -> createDefault()), the lambda is only invoked lazily if the Optional is actually empty. Since opt contains "Hello", the lambda never executes, so "Creating default value" is never printed. In production code, orElseGet is preferred when the fallback involves an expensive computation, because it avoids unnecessary execution.

Q8.
map() applies a function to each element of a stream and wraps the result in the stream. If the mapping function returns a collection like List<Product>, the result would be Stream<List<Product>> — a stream of lists, which is nested and difficult to work with. flatMap() does the same transformation but then flattens the result by one level, merging all the inner collections into a single continuous stream, giving you Stream<Product> directly.
List<String> allProductNames = orders.stream()
    .flatMap(order -> order.getProducts().stream())
    .map(Product::getName)
    .collect(Collectors.toList());

Q9. 
The output will be:
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20
This output demonstrates lazy evaluation, which means that intermediate operations like filter() and map() do not execute at all when the stream is created. They only begin processing when a terminal operation is called — in this case findFirst(). Once the terminal operation triggers execution, elements are processed one at a time through the entire pipeline. The number 1 is tested by filter and rejected because it is odd. The number 2 passes the filter and is then mapped to 20. Since findFirst() only needs one result, it stops immediately after finding 2 and never processes 3, 4, or 5. This short-circuit behaviour combined with lazy evaluation makes streams highly efficient for large data sets.

Q10.
There are two problems in this code. The first is in processProducts, where the code uses isPresent() followed by get(). This is considered an anti-pattern when using Optional because it is essentially the same as a null check and defeats the purpose of Optional's expressive API. The idiomatic way to handle this is to use ifPresent() with a lambda, which only executes if the value is present.
The second problem is in calculateTotal, where the method returns null instead of Optional.empty() when the product list is null or empty. Returning null from a method that is declared to return Optional is a serious misuse of Optional — the entire point of Optional is to eliminate null returns. Any caller of this method expecting an Optional would have to null-check it anyway, which breaks the contract.

public void processProducts(Optional<List<Product>> productsOpt) {
    productsOpt.ifPresent(products -> products.forEach(this::process));
}
The corrected version would look like:
public Optional<BigDecimal> calculateTotal(List<Product> products) {
    if (products == null || products.isEmpty()) {
        return Optional.empty();
    }
    BigDecimal total = products.stream()
        .map(Product::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return Optional.of(total);
}