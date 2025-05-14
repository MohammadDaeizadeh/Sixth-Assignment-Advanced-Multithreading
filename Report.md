# Theoretical Questions 📝
```declarative

    import java.util.concurrent.atomic.AtomicInteger;

    public class AtomicDemo {
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    private static int normalCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                atomicCounter.incrementAndGet();
                normalCounter++;
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Atomic Counter: " + atomicCounter);
        System.out.println("Normal Counter: " + normalCounter);
    }
}
```

### What output do you get from the program? Why? 🤔
**Output:**
```declarative

Atomic Counter: 2000000
Normal Counter: ["Number less than 2000000"]

```
**Reason:** \
`AtomicInteger` supports atomic operations, and `incrementAndGet()` runs in a **thread-safe** manner
so the final value of `atomicCounter` will be exactly `2,000,000`. On the other hand `normalCounter++`
is a **non-atomic** operation, and in multithreaded environment it can result in a **race condition**, causing the final
value to be less than `2,000,000`.

### What is the purpose of AtomicInteger in this code? ☢️
The purpose of using `AtomicInteger` is to perform **thread-safe** operations without requiring a `synchronized` lock.
It supports atomic operations and prevents thread interference.



### What thread-safety guarantees does atomicCounter.incrementAndGet() provide? 👷🏻
This method guarantees that the increment operation (`++`) is performed **atomically**,
that means:
- No thread interference occurs.
- Changes are immediately visible to all threads.
- it uses low-level mechanisms like **CAS (Compare-And-Swap)** to avoid locks.

### In which situations would using a lock be a better choice than an atomic variable? 🔐

1. When the target operation is more complex than a simple increment or compare (e.g. modifying multiple variables at once)
2. When complex synchronization conditions are needed (e.g., `wait`/`notify`).
3. When lock-based synchronization performs better under high contention
(since atomic operations may retry repeatedly in such cases).

### Besides AtomicInteger, what other data types are available in the java.util.concurrent.atomic package? 📦

- `AtomicLong`(for `Long` values).
- `AtomicBoolean`(for `boolean` values).
- `AtomicReference` (for generic object references).
- `AtomicIntegerArray` and `AtomicLongArray` (for arrays).
- `AtomicStampedReference` and `AtomicMarkableReference` (to solve the **ABA problem** in lock-free algorithms).



