Q1.
JVM is the Java Virtual Machine. It is the engine that actually runs the compiled Java bytecode. It is platform-specific, meaning there are different JVMs for Windows, Mac, and Linux.
JRE is the Java Runtime Environment. It includes the JVM plus the standard Java libraries needed to run a Java program. If we only want to run a Java program, JRE is enough.
JDK is the Java Development Kit. It includes everything in JRE plus development tools like the compiler (javac) and debugger. We need JDK if we want to write and compile Java code.
To run a compiled Java program, we only need JRE.

Q2. 10

Q3. 100

Q4. 
true
false
true

Q5. 
A final variable means its value cannot be changed after it is assigned. It becomes a constant.
A final method means it cannot be overridden by subclasses.
A final class means it cannot be extended or subclassed at all. For example, the String class in Java is a final class.

Q6. 
An instance variable belongs to each individual object. Every object has its own copy of the instance variable, so changing it in one object does not affect other objects.
A static variable belongs to the class itself, not to any specific object. All objects of the class share the same copy of the static variable.
A good example of using a static variable is a counter that tracks how many objects have been created, like static int count = 0 that gets incremented in the constructor.

Q7. 
It will not compile. Because x is declared as a final variable and assigned the value 10, trying to reassign it with x = 20 will cause a compile error saying "cannot assign a value to final variable x".

Q8. 
Encapsulation means hiding the internal details of an object and only exposing what is necessary through public methods.
Inheritance means a subclass can inherit the fields and methods of a parent class, promoting code reuse.
Polymorphism means one interface can be used for different types, allowing objects of different classes to be treated as objects of a common superclass.
Abstraction means hiding complex implementation details and only showing the essential features of an object.

Q9. 
Encapsulation is the practice of hiding the internal state of an object by making instance variables private and providing public getter and setter methods to access and modify them.
We do this because it gives us control over the data. For example, in a setter method we can add validation logic to prevent invalid values from being set. It also makes the code more maintainable because if we need to change the internal implementation, we only need to update the class itself without affecting any code that uses it.

Q10. 3

