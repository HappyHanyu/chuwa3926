Q1.
The root of Java's exception hierarchy is `Throwable`. It has two main branches: `Error` and `Exception`.
`Error` represents serious problems that are outside the application's control and should not be caught. Two examples are `OutOfMemoryError`, which occurs when the JVM runs out of heap memory, and `StackOverflowError`, which occurs when the call stack exceeds its limit due to infinite recursion.
`Exception` represents conditions that a program might want to catch and handle. It further splits into checked exceptions (subclasses of `Exception` but not `RuntimeException`) and unchecked exceptions (subclasses of `RuntimeException`). Two examples of checked exceptions are `IOException` and `SQLException`. Two examples of unchecked exceptions are `NullPointerException` and `ArrayIndexOutOfBoundsException`.

Q2.
Checked exceptions are exceptions that the compiler forces you to handle, either by surrounding the code in a try-catch block or by declaring the exception in the method signature with `throws`. They represent recoverable conditions that the caller should be aware of. Examples are `IOException` and `ClassNotFoundException`.
Unchecked exceptions are subclasses of `RuntimeException` and the compiler does not require you to handle them. They usually represent programming mistakes such as null pointer access or illegal arguments. Examples are `NullPointerException` and `IllegalArgumentException`.

Q3.
try
finally
3
Because, When return 1 is reached in the try block, Java does not immediately return. Instead it executes the finally block first. Inside finally, there is another return 3, which overrides the pending return 1. The catch block is never executed because no exception is thrown. The final value returned is 3.

Q4.
throw is used inside a method body to actually throw an exception instance. throws is used in a method signature to declare that the method might throw certain checked exceptions, warning callers to handle them.
public void withdraw(double amount) throws InsufficientBalanceException {
    if (amount > balance) {
        throw new InsufficientBalanceException("Not enough balance");
    }
}

Q5.
The code compiles and runs, but only `RuntimeException("Error 3")` is thrown to the caller. When `Error 1` is thrown in the try block, it is caught by the catch block, which then throws `Error 2`. However, before `Error 2` can propagate, the `finally` block runs and throws `Error 3`. In Java, an exception thrown inside `finally` suppresses any previously pending exception, so `Error 2` is silently discarded and only `Error 3` is seen by the caller.

Q6.
An Enum cannot extend another class because every enum implicitly extends `java.lang.Enum`, and Java does not support multiple class inheritance. Since the parent class slot is already taken, no further extension is possible.
An Enum can implement interfaces. This is a common and useful pattern that allows enums to fulfil a contract and be used polymorphically. Each enum constant can even provide its own implementation of the interface methods.

Q7.
PENDING -> 0 -> 0
PROCESSING -> 1 -> 1
COMPLETED -> 2 -> 2
name() returns the enum constant's name as a string. getCode() returns the custom code field passed through the constructor. ordinal() returns the zero-based position of the constant in the enum declaration order.

Q8.
Aggregation is a "has-a" relationship where the child object can exist independently of the parent. The lifecycle of the child is not controlled by the parent. The child is usually created outside and passed into the parent. In UML it is represented by an empty diamond. Example: a Department has Professors, but professors can exist without the department.
Composition is a stronger "has-a" relationship where the child cannot exist independently of the parent. The parent controls the lifecycle of the child — when the parent is destroyed, the child is destroyed too. The child is created inside the parent. In UML it is represented by a filled diamond. Example: a House is composed of Rooms; rooms cannot exist without the house.

Q9.
This is Aggregation. The Book object b1 is created outside of Library and then passed into it via addBook(). The Library does not control the lifecycle of Book — b1 still exists as an independent object even if the Library is destroyed. The relationship is a loose "has-a" where the parts can live on their own.

Q10.
The three key elements are: a private static field that holds the single instance, a private constructor that prevents external instantiation, and a public static getInstance() method that returns the single instance.
The constructor must be private to prevent any outside code from calling new Singleton() and creating additional instances, which would break the guarantee that only one instance exists.

Q11.
In Eager Initialization, the instance is created at class loading time, before getInstance() is ever called. This is simple and thread-safe but wastes memory if the instance is never used.
In Lazy Initialization, the instance is only created the first time getInstance() is called. This saves memory but requires careful synchronization to be thread-safe.
Eager Initialization creates the instance first.

Q12.
No, it is not correct. There are two problems. First, the constructor is public instead of private, which allows anyone to call new Singleton() and create multiple instances, completely breaking the Singleton pattern. Second, the getInstance() method is not thread-safe — if two threads simultaneously check if (instance == null) before either has created the instance, both could proceed to create separate instances.
The fix is to make the constructor private and use synchronized or the Bill Pugh static inner class pattern for thread safety:
public class Singleton {
    private static Singleton instance;

    private Singleton() { }  // private constructor

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}