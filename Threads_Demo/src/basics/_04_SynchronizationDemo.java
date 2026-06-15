package basics;

// ============================================================
// TOPIC: Synchronization — Race Condition & Mutual Exclusion
// ============================================================
// A RACE CONDITION happens when multiple threads read and write
// a shared variable concurrently without coordination.
// The 'synchronized' keyword ensures only ONE thread executes
// the protected code at a time (mutual exclusion).
//
// Forms:
//   1. synchronized method   → lock is 'this' object
//   2. synchronized block    → lock is a specified object
//   3. static synchronized   → lock is the Class object
// ============================================================

public class _04_SynchronizationDemo {

    // ----------------------------------------------------------
    // Part 1: Race Condition (UNSAFE counter — no synchronization)
    // ----------------------------------------------------------
    static class UnsafeCounter {
        int count = 0;

        public void increment() {
            count++; // NOT atomic: read → add → write (3 operations)
        }
    }

    // ----------------------------------------------------------
    // Part 2: Synchronized Method (SAFE counter)
    // ----------------------------------------------------------
    static class SafeCounterMethod {
        int count = 0;

        // 'synchronized' on a method locks on 'this' object
        public synchronized void increment() {
            count++;
        }
    }

    // ----------------------------------------------------------
    // Part 3: Synchronized Block (finer-grained locking)
    // ----------------------------------------------------------
    static class SafeCounterBlock {
        int count = 0;
        private final Object lock = new Object(); // dedicated lock object

        public void increment() {
            // Only the critical section is locked (better throughput)
            synchronized (lock) {
                count++;
            }
        }
    }

    // ----------------------------------------------------------
    // Helper: run N threads each incrementing a counter M times
    // ----------------------------------------------------------
    static void runThreads(Runnable task, int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(task);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Synchronization Demo ===\n");

        final int THREADS = 10;
        final int INCREMENTS_PER_THREAD = 1000;
        final int EXPECTED = THREADS * INCREMENTS_PER_THREAD;

        // ----------------------------------------------------------
        // Demo 1: Race Condition — result is UNPREDICTABLE
        // ----------------------------------------------------------
        UnsafeCounter unsafe = new UnsafeCounter();
        runThreads(() -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) unsafe.increment();
        }, THREADS);

        System.out.println("--- Race Condition (UnsafeCounter) ---");
        System.out.println("Expected : " + EXPECTED);
        System.out.println("Actual   : " + unsafe.count);
        System.out.println("LOST increments: " + (EXPECTED - unsafe.count));
        System.out.println("(Result varies each run due to race condition)\n");

        // ----------------------------------------------------------
        // Demo 2: Synchronized Method — result is ALWAYS correct
        // ----------------------------------------------------------
        SafeCounterMethod safeMethod = new SafeCounterMethod();
        runThreads(() -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) safeMethod.increment();
        }, THREADS);

        System.out.println("--- Synchronized Method (SafeCounterMethod) ---");
        System.out.println("Expected : " + EXPECTED);
        System.out.println("Actual   : " + safeMethod.count);
        System.out.println("Correct  : " + (safeMethod.count == EXPECTED) + "\n");

        // ----------------------------------------------------------
        // Demo 3: Synchronized Block — result is ALWAYS correct
        // ----------------------------------------------------------
        SafeCounterBlock safeBlock = new SafeCounterBlock();
        runThreads(() -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) safeBlock.increment();
        }, THREADS);

        System.out.println("--- Synchronized Block (SafeCounterBlock) ---");
        System.out.println("Expected : " + EXPECTED);
        System.out.println("Actual   : " + safeBlock.count);
        System.out.println("Correct  : " + (safeBlock.count == EXPECTED) + "\n");

        // ----------------------------------------------------------
        // Demo 4: Static Synchronized Method — lock is the Class object
        // ----------------------------------------------------------
        System.out.println("--- Static Synchronized Method ---");
        Thread t1 = new Thread(StaticCounter::increment, "T1");
        Thread t2 = new Thread(StaticCounter::increment, "T2");
        t1.start(); t2.start();
        t1.join();  t2.join();
        System.out.println("StaticCounter.count after 2 threads each incrementing once: "
                + StaticCounter.count);

        // -------------------------------------------------------
        // KEY POINTS:
        // - count++ is NOT atomic — it is read+add+write (3 steps)
        // - synchronized method:  coarse-grained; locks entire method
        // - synchronized block:   fine-grained; locks only critical section
        // - static synchronized:  lock is MyClass.class (shared by ALL instances)
        // - Re-entrant: a thread can re-enter a synchronized block it already holds
        // -------------------------------------------------------
        System.out.println("\n=== Synchronization Demo Complete ===");
    }

    // Static counter to demonstrate static synchronized
    static class StaticCounter {
        static int count = 0;

        public static synchronized void increment() {
            count++; // lock is StaticCounter.class
            System.out.println(Thread.currentThread().getName() + " incremented static count to " + count);
        }
    }
}

