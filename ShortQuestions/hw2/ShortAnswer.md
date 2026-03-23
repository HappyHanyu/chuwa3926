Q1.
Method overloading is when a class defines multiple methods with the same name but different parameter lists — different number of parameters, different types, or different order. It is resolved at compile time, so it belongs to compile-time polymorphism (also called static polymorphism).
Method overriding is when a subclass provides its own implementation of a method that is already defined in the parent class, keeping the exact same method signature. It is resolved at runtime based on the actual object type, so it belongs to runtime polymorphism (also called dynamic polymorphism).

Q2. Bark!
Even though a is declared as type Animal, the actual object created is new Dog(). Java uses dynamic dispatch at runtime, meaning the JVM looks at the actual type of the object, not the declared type of the variable, to decide which method to call. Since Dog overrides makeSound(), the Dog's version is called.

Q3. 
Java does not support multiple inheritance with classes because it would cause ambiguity — if two parent classes both define a method with the same name, the compiler cannot determine which one the child class should inherit. This is known as the Diamond Problem, where a class inherits from two classes that both share a common ancestor, creating a diamond-shaped inheritance graph with conflicting method definitions.
Java solves this by allowing multiple inheritance only through interfaces. Since Java 8, interfaces can have default methods, which could still cause the same conflict. Java resolves this by requiring the implementing class to explicitly override the conflicting method and choose which interface's version to call using the syntax InterfaceName.super.methodName().

Q4. 
The code will fail to compile. Shape is an abstract class, and abstract classes cannot be instantiated directly. The line Shape s = new Shape() will produce a compile-time error saying "Shape is abstract; cannot be instantiated". To use Shape, you must create a concrete subclass that implements the abstract method getArea().

Q5. 
An abstract class can have instance fields, constructors, and both abstract and concrete methods. A class can only extend one abstract class. An interface cannot have instance fields or constructors, and before Java 8 all its methods were implicitly abstract. A class can implement multiple interfaces.
You would prefer an interface when you want to define a capability or behaviour that can be shared across completely unrelated classes. For example, both Bird and Airplane can implement a Flyable interface even though they share no common parent class. Interfaces are better for defining contracts that multiple unrelated types should fulfil.

Q6. 
Animal eating
Meow!
Line 1 is upcasting — Cat is assigned to an Animal reference, which is safe and done implicitly. Line 2 calls eat(), which Cat inherits from Animal, so it prints "Animal eating". Line 3 is commented out because you cannot call meow() on an Animal reference even though the actual object is a Cat — the compiler only knows the declared type. Line 4 is downcasting — we explicitly cast a back to Cat after confirming with instanceof. Once downcast, the Cat reference c can access meow(), so it prints "Meow!".

Q7.
The rules for overriding equals() are: it must be reflexive (x.equals(x) is true), symmetric (x.equals(y) implies y.equals(x)), transitive, consistent across multiple calls, and x.equals(null) must return false.
We must also override hashCode() because Java's contract states that if two objects are equal according to equals(), they must return the same hashCode(). Collections like HashMap and HashSet use hashCode() first to find the bucket, then use equals() to confirm. If you override equals() without hashCode(), two logically equal objects could end up in different buckets and never be found as equal in a collection.

Q8. 
In a shallow copy, a new object is created but its fields are copied by reference, not by value. So if Person has a field Address address, the copied Person object points to the same Address object in memory. Modifying the address through one person will affect the other.
In a deep copy, a completely new object is created along with new copies of all referenced objects. So the copied Person gets its own separate Address object. Changes to one person's address will not affect the other. Deep copy is more expensive but provides true independence between original and copy.

Q9. 
Taking off from Flyable
Duck implements both Flyable and Swimmable, and both interfaces have a default method named takeOff(). This creates the same diamond conflict seen in Q3. Java requires Duck to override takeOff() explicitly. Inside its override, Duck calls Flyable.super.takeOff(), which explicitly selects Flyable's version, printing "Taking off from Flyable".

Q10. 
Method A is a valid overload because it has a different number of parameters (three instead of two).
Method B is a valid overload because it has different parameter types (double instead of int).
Method C is NOT a valid overload. It has the same parameter types as the original (int a, int b), and Java does not consider the return type when distinguishing overloaded methods. This will cause a compile error.
Method D is NOT a valid overload. It also has the same parameter types (int x, int y). Changing the parameter names and the access modifier does not make it a valid overload. This will also cause a compile error.