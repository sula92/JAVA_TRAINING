package basics;

import java.util.concurrent.atomic.AtomicInteger;

// ============================================================
// TOPIC: volatile Keyword
// ============================================================
// The JVM allows threads to cache variables in CPU registers/cache.
// A write in Thread-1 may NOT be visible to Thread-2 until the
// cache is flushed — this is the VISIBILITY PROBLEM.
//
// 'volatile' guarantees:
//   1. VISIBILITY  → every read goes to main memory;
//                    every write is flushed to main memory immediately
//   2. ORDERING    → prevents instruction reordering around the variable
//
// What 'volatile' does NOT guarantee:
//   - Atomicity for compound operations like count++ (read-add-write)
//
// Use cases:
//   ✅ Boolean flags (one writer, many readers)
//   ✅ Single reference assignments
//   ❌ Counters → use AtomicInteger or synchronized
// ============================================================

public class _09_VolatileDemo {

    // ----------------------------------------------------------
    // Part 1: volatile boolean flag — stop a thread cleanly
    // ----------------------------------------------------------
    static volatile boolean stopFlag = false; // visible across all threads


    // ----------------------------------------------------------
    // Part 3: volatile vs AtomicInteger for counter
    // ----------------------------------------------------------
    static volatile int volatileCounter = 0;         // NOT safe for ++
    static AtomicInteger atomicCounter = new AtomicInteger(0); // SAFE for ++

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== volatile Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: volatile flag — stops a background thread safely
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: volatile stop flag ---");

        Thread worker = new Thread(() -> {
            int count = 0;
            while (!stopFlag) { // reads stopFlag from main memory each time
                count++;
            }
            System.out.println("  Worker stopped after " + count + " iterations.");
        }, "Worker");

        worker.start();
        Thread.sleep(100);  // let worker run for a bit
        stopFlag = true;    // write to main memory → worker will see it
        worker.join();
        System.out.println("  volatile flag test passed.\n");

        // ----------------------------------------------------------
        // Demo 2: volatile counter — NOT atomic for compound ops
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: volatile counter is NOT thread-safe for ++ ---");

        final int THREADS = 5;
        final int OPS = 1000;
        Thread[] threads = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPS; j++) {
                    volatileCounter++; // NOT atomic — race condition!
                }
            });
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("  Expected with volatile++  : " + (THREADS * OPS));
        System.out.println("  Actual with volatile++    : " + volatileCounter);
        System.out.println("  (Likely < expected due to race condition)\n");

        // ----------------------------------------------------------
        // Demo 3: AtomicInteger — truly atomic; correct counter
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: AtomicInteger — thread-safe counter ---");

        Thread[] atomicThreads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < OPS; j++) {
                    atomicCounter.incrementAndGet(); // atomic CAS operation
                }
            });
        }
        for (Thread t : atomicThreads) t.start();
        for (Thread t : atomicThreads) t.join();

        System.out.println("  Expected with AtomicInteger: " + (THREADS * OPS));
        System.out.println("  Actual with AtomicInteger  : " + atomicCounter.get());
        System.out.println("  Correct: " + (atomicCounter.get() == THREADS * OPS) + "\n");

        // ----------------------------------------------------------
        // Demo 4: Singleton with volatile (double-checked locking)
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Double-checked locking with volatile ---");
        System.out.println("  Getting singleton instance...");
        LazySingleton s1 = LazySingleton.getInstance();
        LazySingleton s2 = LazySingleton.getInstance();
        System.out.println("  s1 == s2 (same instance): " + (s1 == s2));

        // -------------------------------------------------------
        // KEY POINTS:
        // - volatile ensures visibility (reads/writes go to main memory)
        // - volatile does NOT make compound ops (++) atomic
        // - Use AtomicInteger/AtomicLong for counters
        // - Use volatile for simple flag variables (boolean, reference)
        // - Double-checked locking REQUIRES volatile to prevent
        //   seeing a half-constructed object
        // -------------------------------------------------------
        System.out.println("\n=== volatile Demo Complete ===");
    }

    // Double-checked locking pattern requires 'volatile' on the instance field
    static class LazySingleton {
        // WITHOUT volatile, another thread could see a partially constructed object
        private static volatile LazySingleton instance;

        private LazySingleton() {
            System.out.println("  LazySingleton created in " + Thread.currentThread().getName());
        }

        public static LazySingleton getInstance() {
            if (instance == null) {                // first check (no lock)
                synchronized (LazySingleton.class) {
                    if (instance == null) {         // second check (with lock)
                        instance = new LazySingleton();
                    }
                }
            }
            return instance;
        }
    }
}

