package basics;

import java.util.concurrent.*;

// ============================================================
// TOPIC: ExecutorService and Thread Pools
// ============================================================
// Creating a raw Thread for every task is expensive.
// A THREAD POOL pre-creates threads and reuses them.
//
// Executors factory methods:
//   newFixedThreadPool(n)       → exactly n threads; extras queue
//   newCachedThreadPool()       → grows/shrinks dynamically
//   newSingleThreadExecutor()   → one thread; sequential execution
//   newScheduledThreadPool(n)   → supports delay & periodic tasks
//
// Key operations:
//   execute(Runnable)           → fire-and-forget (no result)
//   submit(Runnable/Callable)   → returns a Future
//   shutdown()                  → graceful stop (queued tasks run)
//   shutdownNow()               → forceful stop (interrupts tasks)
//   awaitTermination(t, unit)   → wait up to t for all tasks to end
// ============================================================

public class _07_ExecutorServiceDemo {

    // Simple task that prints its name and simulates work
    static Runnable makeTask(String name, int sleepMs) {
        return () -> {
            System.out.println("  [" + Thread.currentThread().getName() + "] "
                    + name + " started");
            try { Thread.sleep(sleepMs); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("  [" + Thread.currentThread().getName() + "] "
                    + name + " finished");
        };
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== ExecutorService Demo ===\n");

        // ----------------------------------------------------------
        // 1. Fixed Thread Pool — exactly N threads
        // ----------------------------------------------------------
        System.out.println("--- 1. FixedThreadPool(2) with 5 tasks ---");
        ExecutorService fixed = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 5; i++) {
            fixed.execute(makeTask("Task-" + i, 400));
        }

        fixed.shutdown(); // stop accepting new tasks
        fixed.awaitTermination(10, TimeUnit.SECONDS); // wait for running tasks
        System.out.println("  FixedThreadPool done.\n");

        // ----------------------------------------------------------
        // 2. Cached Thread Pool — grows as needed; idle threads expire
        // ----------------------------------------------------------
        System.out.println("--- 2. CachedThreadPool with 4 tasks ---");
        ExecutorService cached = Executors.newCachedThreadPool();

        for (int i = 1; i <= 4; i++) {
            cached.execute(makeTask("CachedTask-" + i, 300));
        }

        cached.shutdown();
        cached.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("  CachedThreadPool done.\n");

        // ----------------------------------------------------------
        // 3. Single Thread Executor — one thread; sequential order
        // ----------------------------------------------------------
        System.out.println("--- 3. SingleThreadExecutor with 3 tasks ---");
        ExecutorService single = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 3; i++) {
            single.execute(makeTask("SingleTask-" + i, 200));
        }

        single.shutdown();
        single.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("  SingleThreadExecutor done.\n");

        // ----------------------------------------------------------
        // 4. Scheduled Thread Pool — run with delay or periodically
        // ----------------------------------------------------------
        System.out.println("--- 4. ScheduledThreadPool ---");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Run once after 500ms delay
        scheduler.schedule(
            () -> System.out.println("  [Scheduler] One-shot task ran after 500ms delay"),
            500, TimeUnit.MILLISECONDS
        );

        // Run every 600ms (fixed rate — based on start time)
        ScheduledFuture<?> periodic = scheduler.scheduleAtFixedRate(
            () -> System.out.println("  [Scheduler] Periodic task @ " + System.currentTimeMillis()),
            0, 600, TimeUnit.MILLISECONDS
        );

        Thread.sleep(2000); // let the periodic task run a few times
        periodic.cancel(false); // cancel the repeating task

        scheduler.shutdown();
        scheduler.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("  ScheduledThreadPool done.\n");

        // ----------------------------------------------------------
        // 5. submit() vs execute() — submit returns a Future
        // ----------------------------------------------------------
        System.out.println("--- 5. submit() returns a Future ---");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<?> f = pool.submit(() -> {
            Thread.sleep(300);
            System.out.println("  [Submit] Runnable task completed");
            return null; // Callable-style; can also pass Runnable
        });

        System.out.println("  isDone() before get(): " + f.isDone());
        try { f.get(); } catch (ExecutionException e) { e.printStackTrace(); }
        System.out.println("  isDone() after  get(): " + f.isDone());

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        // -------------------------------------------------------
        // KEY POINTS:
        // - Always call shutdown() to release thread pool resources
        // - execute()  → void; use for fire-and-forget Runnables
        // - submit()   → Future; use to retrieve result or check completion
        // - FixedThreadPool: predictable resource usage
        // - CachedThreadPool: good for many short-lived tasks
        // - SingleThreadExecutor: guaranteed sequential execution
        // - ScheduledThreadPool: replaces Timer; better error handling
        // -------------------------------------------------------
        System.out.println("\n=== ExecutorService Demo Complete ===");
    }
}

