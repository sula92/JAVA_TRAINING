package basics;

// ============================================================
// TOPIC: Thread Lifecycle (States)
// ============================================================
// A Java thread passes through these states (Thread.State enum):
//   NEW          → created but start() not yet called
//   RUNNABLE     → after start(); actively running or ready
//   BLOCKED      → waiting to acquire a monitor lock
//   WAITING      → waiting indefinitely (wait(), join())
//   TIMED_WAITING→ waiting with a timeout (sleep(ms), wait(ms), join(ms))
//   TERMINATED   → run() completed or exception thrown
// ============================================================

public class _03_ThreadLifecycleDemo {

    // A shared lock object used to demonstrate BLOCKED state
    private static final Object sharedLock = new Object();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Thread Lifecycle Demo ===\n");

        // ---------------------------------------------------------
        // 1. NEW State — thread object created, start() NOT called
        // ---------------------------------------------------------
        Thread t1 = new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "LifecycleThread");

        System.out.println("After new Thread()   → State: " + t1.getState()); // NEW

        // ---------------------------------------------------------
        // 2. RUNNABLE State — after start()
        // ---------------------------------------------------------
        t1.start();
        System.out.println("After t1.start()     → State: " + t1.getState()); // RUNNABLE

        // ---------------------------------------------------------
        // 3. TIMED_WAITING State — thread is sleeping
        // ---------------------------------------------------------
        Thread.sleep(200); // give t1 time to enter sleep
        System.out.println("While t1 sleeping    → State: " + t1.getState()); // TIMED_WAITING

        // ---------------------------------------------------------
        // 4. TERMINATED State — run() has finished
        // ---------------------------------------------------------
        t1.join(); // wait for t1 to finish
        System.out.println("After t1.join()      → State: " + t1.getState()); // TERMINATED

        // ---------------------------------------------------------
        // 5. BLOCKED State — thread waiting for a monitor lock
        // ---------------------------------------------------------
        System.out.println("\n--- Demonstrating BLOCKED state ---");

        // Thread that holds the lock for 1 second
        Thread lockHolder = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("lockHolder acquired the lock.");
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("lockHolder released the lock.");
            }
        }, "LockHolder");

        // Thread that tries to acquire the same lock → will be BLOCKED
        Thread blocker = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("blocker acquired the lock.");
            }
        }, "Blocker");

        lockHolder.start();
        Thread.sleep(100);   // let lockHolder grab the lock first
        blocker.start();
        Thread.sleep(100);   // let blocker attempt to acquire the lock

        System.out.println("Blocker state        → State: " + blocker.getState()); // BLOCKED

        lockHolder.join();
        blocker.join();

        // ---------------------------------------------------------
        // 6. WAITING State — thread waiting with no timeout (join())
        // ---------------------------------------------------------
        System.out.println("\n--- Demonstrating WAITING state ---");

        Thread longTask = new Thread(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "LongTask");

        Thread waiter = new Thread(() -> {
            try {
                longTask.join(); // waits indefinitely → WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Waiter");

        longTask.start();
        waiter.start();
        Thread.sleep(200);

        System.out.println("Waiter state         → State: " + waiter.getState()); // WAITING

        waiter.interrupt(); // stop waiting
        longTask.interrupt();
        waiter.join();
        longTask.join();

        // -------------------------------------------------------
        // KEY POINTS:
        // - Always call start(), never run() directly
        // - BLOCKED   → thread is queued for a synchronized lock
        // - WAITING   → thread is dormant until explicitly notified / joined
        // - TIMED_WAITING → like WAITING but with an expiry timeout
        // - getState() returns Thread.State enum at that instant
        // -------------------------------------------------------
        System.out.println("\n=== Lifecycle Demo Complete ===");
    }
}

