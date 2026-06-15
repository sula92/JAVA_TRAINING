package basics;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

// ============================================================
// TOPIC: Deadlock — Causes and Prevention
// ============================================================
// A DEADLOCK occurs when two or more threads permanently block
// each other because each holds a lock that the other needs.
//
// Four Coffman Conditions (all must hold for deadlock):
//   1. Mutual Exclusion  — resource held by only one thread
//   2. Hold and Wait     — thread holds one lock, waits for another
//   3. No Preemption     — locks cannot be forcibly taken
//   4. Circular Wait     — T1 waits for T2, T2 waits for T1
//
// Prevention Strategies:
//   A. Lock Ordering     — always acquire locks in the same order
//   B. tryLock()         — attempt with timeout; back off if busy
// ============================================================

public class _06_DeadlockDemo {

    // Two shared locks
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    // --------------------------------------------------------
    // Part 1: DEMONSTRATE a Deadlock
    // --------------------------------------------------------
    // Thread-1: acquires lockA then tries lockB
    // Thread-2: acquires lockB then tries lockA  → circular wait!
    // --------------------------------------------------------
    static void demonstrateDeadlock() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("  T1: acquired lockA");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("  T1: waiting for lockB...");
                synchronized (lockB) {
                    System.out.println("  T1: acquired lockB — done!");
                }
            }
        }, "T1-Deadlock");

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("  T2: acquired lockB");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("  T2: waiting for lockA...");
                synchronized (lockA) {
                    System.out.println("  T2: acquired lockA — done!");
                }
            }
        }, "T2-Deadlock");

        t1.start();
        t2.start();

        // Wait only 2 seconds — if still blocked, it's a deadlock
        t1.join(2000);
        t2.join(2000);

        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("  *** DEADLOCK DETECTED — both threads are stuck! ***");
            t1.interrupt();
            t2.interrupt();
        }
    }

    // --------------------------------------------------------
    // Part 2: Prevention via LOCK ORDERING
    // --------------------------------------------------------
    // Both threads always acquire lockA first, then lockB.
    // This breaks the circular-wait condition.
    // --------------------------------------------------------
    static void preventWithLockOrdering() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {             // always lockA first
                System.out.println("  T1: acquired lockA");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (lockB) {         // then lockB
                    System.out.println("  T1: acquired lockB — DONE (no deadlock)");
                }
            }
        }, "T1-Ordered");

        Thread t2 = new Thread(() -> {
            synchronized (lockA) {             // same order: lockA first
                System.out.println("  T2: acquired lockA");
                synchronized (lockB) {         // then lockB
                    System.out.println("  T2: acquired lockB — DONE (no deadlock)");
                }
            }
        }, "T2-Ordered");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    // --------------------------------------------------------
    // Part 3: Prevention via tryLock() with timeout
    // --------------------------------------------------------
    static void preventWithTryLock() throws InterruptedException {

        ReentrantLock rlA = new ReentrantLock();
        ReentrantLock rlB = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            try {
                if (rlA.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("  T1: acquired rlA");
                        Thread.sleep(100);
                        if (rlB.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("  T1: acquired rlB — DONE");
                            } finally { rlB.unlock(); }
                        } else {
                            System.out.println("  T1: could not acquire rlB — backing off");
                        }
                    } finally { rlA.unlock(); }
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "T1-TryLock");

        Thread t2 = new Thread(() -> {
            try {
                if (rlB.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("  T2: acquired rlB");
                        Thread.sleep(100);
                        if (rlA.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("  T2: acquired rlA — DONE");
                            } finally { rlA.unlock(); }
                        } else {
                            System.out.println("  T2: could not acquire rlA — backing off");
                        }
                    } finally { rlB.unlock(); }
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "T2-TryLock");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Deadlock Demo ===\n");

        System.out.println("--- Part 1: Demonstrating Deadlock ---");
        demonstrateDeadlock();
        System.out.println();

        System.out.println("--- Part 2: Prevention via Lock Ordering ---");
        preventWithLockOrdering();
        System.out.println();

        System.out.println("--- Part 3: Prevention via tryLock() ---");
        preventWithTryLock();

        // -------------------------------------------------------
        // KEY POINTS:
        // - Deadlock needs: mutual exclusion, hold-and-wait,
        //   no preemption, AND circular wait
        // - Lock ordering breaks circular wait (most common fix)
        // - tryLock(timeout) lets threads back off instead of blocking forever
        // - Keep synchronized sections short to reduce contention
        // -------------------------------------------------------
        System.out.println("\n=== Deadlock Demo Complete ===");
    }
}

