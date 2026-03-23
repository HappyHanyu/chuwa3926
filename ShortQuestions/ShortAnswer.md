Q2.
public class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

Q3.
There are four main ways to create threads in Java.
The first way is extending the Thread class. You create a subclass of Thread, override its run()
method with the logic you want to execute, then create an instance of your class and call start().
It's the most straightforward way, but the problem is Java doesn't support multiple
inheritance. Once you extend Thread, you can't extend any other class. So this approach is
very limited. The second way is implementing the Runnable interface. You write a class that
implements Runnable and put your logic in the run() method. Then you pass that Runnable
object into a new Thread and call start(). This is better because your class can still extend
another class, and it also separates the task definition from the thread itself. Since Java 8,
Runnable is a functional interface, so you can just pass a lambda expression directly instead
of creating a whole new class. The third way is implementing Callable with Future. The
limitation of Runnable is that its run() method returns void and can't throw checked
exceptions. Callable solves both problems — its call() method can return a value with a generic
type and can throw exceptions. The fourth way is using a thread pool through ExecutorService.
In real production code, we almost never create threads manually because creating and
destroying threads is expensive. You submit Runnable or Callable tasks to it, and the pool
assigns idle threads to handle them. 

Q4.
| ----   | Runnable           |  Callable |
|------  |------------------  | ----------|
| Return Value | None (`void`)        | Present (`V`) |
| Exceptions   | Cannot throw checked exceptions | Can throw checked exceptions |
| Method Name  | `run()`            | `call()` |
| Usage With   | Thread / Executor | Future / ExecutorService |


Q5.
`t.start()`: The JVM creates a new thread and executes `run()` within that new thread.
`t.run()`: The method is called directly within the current thread; no new thread is created.

Q6.
It is recommended to use `Runnable` for the following reasons: due to Java's single-inheritance restriction, implementing an interface offers greater flexibility.
`Runnable` tasks are reusable and can be passed to different threads or thread pools.
It facilitates a better separation between the "task" itself and the "execution mechanism."

Q7.
NEW → RUNNABLE → (BLOCKED / WAITING / TIMED_WAITING) → TERMINATED
NEW: Created but not yet started
RUNNABLE: Running or waiting for CPU
BLOCKED: Waiting for a synchronized lock
WAITING: Waiting for a notification (wait(), join())
TIMED_WAITING: Waiting with a timeout (sleep(), wait(ms))
TERMINATED: Execution completed

Q8.
// deadlock
Object lockA = new Object(), lockB = new Object();
Thread t1 = new Thread(() -> {
    synchronized (lockA) {
        sleep(100);
        synchronized (lockB) { System.out.println("T1 done"); }
    }
});
Thread t2 = new Thread(() -> {
    synchronized (lockB) {
        sleep(100);
        synchronized (lockA) { System.out.println("T2 done"); }
    }
});

// Solution: Standardize the Locking Order 
Thread t2Fixed = new Thread(() -> {
    synchronized (lockA) {
        synchronized (lockB) { System.out.println("T2 done"); }
    }
});

Q9.
wait() / notify() / notifyAll() — Used within `synchronized` blocks
ReentrantLock + Condition — await() / signal()
BlockingQueue — Producer-Consumer Model
CountDownLatch, CyclicBarrier, Semaphore
volatile — Sharing Simple Status Flags

Q10.
Object Lock: `synchronized(this)` or `synchronized` instance methods → Locks a specific object instance.
Class Lock: `synchronized(MyClass.class)` or `static synchronized` methods → Locks the entire class (shared by all instances).

Q11.
The `join()` method causes the current thread to wait for the target thread to complete its execution before continuing.
Thread t = new Thread(() -> System.out.println("Child"));
t.start();
t.join(); // The main thread waits here for t to complete.
System.out.println("Main continues");

Q12.
The `yield()` method signals to the scheduler, "I am willing to relinquish the CPU," though the scheduler may choose to ignore this suggestion. The thread transitions from the RUNNING state back to the RUNNABLE state and may be rescheduled immediately. It is primarily used for testing or performance tuning and is rarely employed in practical applications.

Q13.
A thread pool is a collection of pre-created worker threads that are reused to execute multiple tasks, instead of creating a new thread for every task.
In Java, there are typically four common types of thread pools: FixedThreadPool, CachedThreadPool, SingleThreadExecutor, and ScheduledThreadPool. The FixedThreadPool employs a fixed number of threads to process tasks, making it suitable for scenarios with stable workloads; the CachedThreadPool dynamically creates and reclaims threads as needed, making it ideal for handling a large volume of short-lived tasks; the SingleThreadExecutor utilizes only a single thread, thereby guaranteeing that tasks are executed sequentially; and the ScheduledThreadPool supports delayed execution and scheduled tasks. Fundamentally, all these thread pools are implemented based on the `ThreadPoolExecutor`, differing only in their specific configurations.
Within a thread pool, the TaskQueue (also known as the work queue) serves as a critical component used to store tasks that have been submitted but not yet executed by a thread. When a task is submitted, if the current number of running threads is less than the `corePoolSize`, a new thread is created to execute the task; if the core threads are already fully occupied, the task is placed into the TaskQueue to wait; if the queue also becomes full, the thread pool continues to create additional threads until the `maximumPoolSize` is reached; should it still be unable to process the task, a rejection policy is triggered. Common types of TaskQueues include `LinkedBlockingQueue` (an unbounded queue, which can easily lead to task accumulation), `ArrayBlockingQueue` (a bounded queue, suitable for flow control), and `SynchronousQueue` (which does not store tasks but hands them off directly to a thread for processing). Consequently, the behavior of a thread pool depends significantly on both its thread count configuration and the specific type of TaskQueue employed.

Q14.
Library: java.util.concurrent
Interface: Executor → ExecutorService → ThreadPoolExecutor

Q15.
We typically use an `ExecutorService` to submit tasks to a thread pool; common methods for doing so include `execute()` and `submit()`. The `execute()` method is used to submit `Runnable` tasks and does not return a result; conversely, `submit()` can accept either `Runnable` or `Callable` tasks and returns a `Future` object, which allows for retrieving the task's execution result or checking its status. For instance, one might create a thread pool using `Executors.newFixedThreadPool()`, then call `submit()` to dispatch tasks, and finally invoke `shutdown()` to gracefully terminate the pool when it is no longer needed.

Q16.
The primary advantage of a thread pool lies in its ability to significantly enhance both performance and system stability. First, by reusing threads, it eliminates the overhead associated with the frequent creation and destruction of threads, thereby boosting execution efficiency. Second, it enables the control of system resource usage by limiting the number of active threads, thereby preventing system crashes caused by an excessive number of concurrent threads. Furthermore, thread pools are typically utilized in conjunction with task queues to smooth out workload fluctuations—effectively "leveling the peaks and filling the valleys"—and to handle high-concurrency requests more gracefully. Finally, thread pools provide a unified mechanism for thread management, making task scheduling, exception handling, and monitoring more convenient and controllable. Consequently, in high-concurrency systems, the thread pool serves as a pivotal core component.

Q17.
Both `shutdown()` and `shutdownNow()` are used to terminate a thread pool, but they exhibit different behaviors. `shutdown()` performs a *graceful shutdown*: it ceases accepting new tasks but continues to execute all tasks that have already been submitted—including those currently in the queue—until every task has been completed, at which point the thread pool is truly shut down. In contrast, `shutdownNow()` performs an *immediate shutdown*: it attempts to interrupt the threads currently in execution, clears the task queue, and returns any unexecuted tasks to the caller. However, it is important to note that whether a thread can actually be interrupted depends on whether the task itself correctly responds to interrupt signals (for instance, by checking `Thread.interrupted()`). Therefore, it is generally recommended to prioritize the use of `shutdown()`, reserving `shutdownNow()` only for situations where an immediate halt is required.

Q18.
The Atomic classes in Java constitute a group of classes located within the `java.util.concurrent.atomic` package, designed to provide lock-free, thread-safe operations in multi-threaded environments. They are implemented based on the CAS (Compare-And-Swap) mechanism, thereby avoiding the performance overhead associated with using `synchronized` keywords or explicit locks.

Common Atomic classes can be broadly categorized into three groups: the first group consists of atomic classes for primitive types, such as `AtomicInteger`, `AtomicLong`, and `AtomicBoolean`; the second group comprises atomic classes for array types, such as `AtomicIntegerArray` and `AtomicLongArray`; and the third group includes atomic classes for reference types, such as `AtomicReference`, `AtomicStampedReference` (used to address the ABA problem), and `AtomicMarkableReference`.

Example:
AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();     // ++count
count.getAndIncrement();     // count++
count.addAndGet(5);          // count += 5
count.compareAndSet(5, 10);  // CAS: if(count==5) count=10

Atomic classes are suitable for high-concurrency scenarios involving simple operations—such as counters, status flags, or statistical data. When only atomic operations on a single variable are required, using Atomic classes is more efficient than using locks; however, if complex consistency operations involving multiple variables are involved, locks (such as `synchronized` or `ReentrantLock`) are still necessary to ensure overall atomicity.

Q19.
Concurrent collections are a set of thread-safe data structures in Java, located within the `java.util.concurrent` package, designed to facilitate safe and efficient data operations in multi-threaded environments. Compared to traditional synchronized collections (such as `Collections.synchronizedList`), concurrent collections typically employ finer-grained locking or lock-free (CAS) mechanisms; this approach ensures thread safety while delivering superior performance and higher concurrency.
Common thread-safe data structures include: `ConcurrentHashMap` (a hash table optimized for high concurrency via segmentation or CAS), `CopyOnWriteArrayList` (suitable for read-heavy, write-light scenarios using a copy-on-write strategy), `CopyOnWriteArraySet`, `ConcurrentLinkedQueue` (a CAS-based lock-free queue), `BlockingQueue` (such as `ArrayBlockingQueue` and `LinkedBlockingQueue`, used in producer-consumer models), and `ConcurrentSkipListMap` (a concurrent Map that supports ordered access). These collections are widely utilized in high-concurrency systems—for instance, in caches, task queues, and messaging systems.

Q20.
First is `synchronized`, a JVM-intrinsic lock that is simple to use, automatically releases the lock, and—thanks to optimizations in modern JVMs—now offers excellent performance. Next is `ReentrantLock`, an explicit lock that provides greater control capabilities—such as interruptible locking (`lockInterruptibly()`), non-blocking lock acquisition (`tryLock()`), and fairness mechanisms—making it suitable for complex concurrency scenarios. Additionally, there is `ReadWriteLock` (e.g., `ReentrantReadWriteLock`), which separates read locks from write locks and can significantly boost performance in scenarios where reads far outnumber writes; `StampedLock`, which offers optimistic read locks to further minimize blocking during read operations; `SpinLock`, which avoids thread context switching by looping while waiting and is ideal for scenarios where the lock is held for very brief durations; and distributed locks (such as those based on Redis), which are used in multi-node systems to ensure global consistency.

Q21.
`Future` is an interface used to represent the result of an asynchronous computation; it allows you to submit a task and retrieve its result at some point in the future. Common methods include `get()` (to block and retrieve the result), `isDone()` (to check if the task is complete), and `cancel()` (to cancel the task). However, a drawback of `Future` is its limited functionality—for instance, it lacks support for task composition, method chaining, and exception handling. `CompletableFuture` serves as an enhancement to `Future`, supporting the chained orchestration of asynchronous tasks (in a functional style) and facilitating convenient task composition, callbacks, and exception handling. Common methods include: `supplyAsync()` (to execute asynchronously and return a result), `runAsync()` (for tasks that do not return a value), `thenApply()` (to transform the result), `thenAccept()` (to consume the result), `thenRun()` (to execute a subsequent action), `thenCombine()` (to merge the results of two tasks), and `exceptionally()` (for exception handling). Consequently, `CompletableFuture` is highly suitable for complex asynchronous workflows, such as microservice invocation chains or the orchestration of concurrent tasks.