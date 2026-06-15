package basics;

// ============================================================
// TOPIC: Thread Priority and Daemon Threads
// ============================================================
// THREAD PRIORITY:
//   Every thread has a priority 1–10 (default 5).
//   Higher priority → scheduler HINTS to run it more often.
//   ⚠ Not a guarantee — OS/JVM can ignore hints.
//   Constants: Thread.MIN_PRIORITY=1, NORM_PRIORITY=5, MAX_PRIORITY=10
//
// DAEMON THREADS:
//   A daemon thread is a background thread.
//   The JVM exits when ONLY daemon threads remain — it does NOT
//   wait for daemon threads to finish (unlike user/non-daemon threads).
//   Use case: background logging, GC, heartbeat monitors.
//   Rule: setDaemon(true) MUST be called BEFORE start().
//
// Thread.yield():
//   Hints to the scheduler to yield the CPU to another thread.
//   Rarely used; not guaranteed to have any effect.
// ============================================================

public class _11_PriorityAndDaemonDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Thread Priority and Daemon Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Thread Priority
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Thread Priority ---");

        Thread lowPriority = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("  [LOW-" + Thread.currentThread().getPriority()
                        + "] iteration " + (i + 1));
                Thread.yield(); // hint to yield; give high-priority thread a chance
            }
        }, "LowPriorityThread");

        Thread highPriority = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("  [HIGH-" + Thread.currentThread().getPriority()
                        + "] iteration " + (i + 1));
            }
        }, "HighPriorityThread");

        // Must set priority BEFORE start()
        lowPriority.setPriority(Thread.MIN_PRIORITY);   // 1
        highPriority.setPriority(Thread.MAX_PRIORITY);  // 10

        System.out.println("  LowPriority  priority: " + lowPriority.getPriority());
        System.out.println("  HighPriority priority: " + highPriority.getPriority());
        System.out.println("  (Note: ordering is a hint — not guaranteed)\n");

        lowPriority.start();
        highPriority.start();
        lowPriority.join();
        highPriority.join();

        // ----------------------------------------------------------
        // Demo 2: Default Thread Priority (inherits from parent)
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 2: Default priority ---");

        Thread defaultThread = new Thread(() ->
            System.out.println("  Default priority: " + Thread.currentThread().getPriority())
        );
        defaultThread.start();
        defaultThread.join();

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); // change main's priority
        Thread childThread = new Thread(() ->
            System.out.println("  Child inherits main's priority: "
                    + Thread.currentThread().getPriority())
        );
        childThread.start();
        childThread.join();
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY); // restore

        // ----------------------------------------------------------
        // Demo 3: Daemon Thread
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 3: Daemon Thread ---");

        Thread daemon = new Thread(() -> {
            int count = 0;
            while (true) { // runs forever, but JVM will kill it when main exits
                try {
                    Thread.sleep(300);
                    System.out.println("  [Daemon] heartbeat #" + (++count)
                            + " | isDaemon: " + Thread.currentThread().isDaemon());
                } catch (InterruptedException e) {
                    System.out.println("  [Daemon] interrupted, stopping.");
                    return;
                }
            }
        }, "DaemonHeartbeat");

        daemon.setDaemon(true); // MUST be set before start()
        daemon.start();

        System.out.println("  [Main] isDaemon(daemon): " + daemon.isDaemon());
        System.out.println("  [Main] isDaemon(main)  : "
                + Thread.currentThread().isDaemon());

        Thread.sleep(1000); // let daemon run for ~3 heartbeats

        System.out.println("\n  [Main] About to finish — JVM will kill daemon automatically.");
        // When main thread finishes and no other user threads are alive,
        // the JVM will terminate the daemon thread automatically.

        // ----------------------------------------------------------
        // Demo 4: User thread keeps JVM alive
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 4: User thread keeps JVM alive ---");

        Thread userThread = new Thread(() -> {
            try {
                System.out.println("  [UserThread] Running for 500ms...");
                Thread.sleep(500);
                System.out.println("  [UserThread] Finished.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "UserThread");

        userThread.setDaemon(false); // this is the default
        userThread.start();
        userThread.join(); // wait for user thread

        // -------------------------------------------------------
        // KEY POINTS:
        // - Priority 1–10; higher = more CPU time (hint, not guarantee)
        // - setPriority() must be called before start()
        // - Child thread inherits parent's priority by default
        // - Daemon threads are killed by JVM when no user threads remain
        // - setDaemon(true) must be called before start()
        // - yield() is a hint to the scheduler; rarely needed in practice
        // -------------------------------------------------------
        System.out.println("\n=== Priority and Daemon Demo Complete ===");
    }
}

