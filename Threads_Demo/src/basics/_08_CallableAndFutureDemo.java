package basics;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// TOPIC: Callable and Future
// ============================================================
// Runnable limitations:
//   - run() cannot return a value
//   - run() cannot throw checked exceptions
//
// Callable<V> solves both:
//   - call() returns a value of type V
//   - call() can throw checked exceptions
//
// Future<V> represents a pending result:
//   - get()                    → blocks until result ready
//   - get(timeout, unit)       → blocks up to timeout
//   - isDone()                 → non-blocking; true if completed
//   - cancel(mayInterrupt)     → attempt to cancel
//   - isCancelled()            → true if cancelled
// ============================================================

public class _08_CallableAndFutureDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Callable and Future Demo ===\n");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // ----------------------------------------------------------
        // 1. Basic Callable — returns a computed value
        // ----------------------------------------------------------
        System.out.println("--- 1. Basic Callable ---");

        Callable<Integer> squareTask = () -> {
            Thread.sleep(500);
            int result = 7 * 7;
            System.out.println("  Callable computed: " + result);
            return result;
        };

        Future<Integer> squareFuture = executor.submit(squareTask);

        System.out.println("  Submitted task. isDone: " + squareFuture.isDone());
        try {
            int result = squareFuture.get(); // blocks until ready
            System.out.println("  Future.get() result: " + result);
            System.out.println("  isDone after get:    " + squareFuture.isDone());
        } catch (ExecutionException e) {
            System.out.println("  Task threw: " + e.getCause().getMessage());
        }

        // ----------------------------------------------------------
        // 2. Multiple Callables — submit all, collect results later
        // ----------------------------------------------------------
        System.out.println("\n--- 2. Multiple Callables (invokeAll) ---");

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            final int id = i;
            tasks.add(() -> {
                Thread.sleep(id * 200L);
                return "Result-" + id + " from " + Thread.currentThread().getName();
            });
        }

        List<Future<String>> futures = executor.invokeAll(tasks); // blocks until ALL done

        for (Future<String> f : futures) {
            try {
                System.out.println("  " + f.get());
            } catch (ExecutionException e) {
                System.out.println("  Task failed: " + e.getCause().getMessage());
            }
        }

        // ----------------------------------------------------------
        // 3. get() with Timeout — avoid waiting forever
        // ----------------------------------------------------------
        System.out.println("\n--- 3. Future.get() with Timeout ---");

        Future<String> slowTask = executor.submit(() -> {
            Thread.sleep(3000); // takes 3 seconds
            return "slow result";
        });

        try {
            String value = slowTask.get(1, TimeUnit.SECONDS); // only wait 1s
            System.out.println("  Got: " + value);
        } catch (TimeoutException e) {
            System.out.println("  TimeoutException — task took too long! Cancelling...");
            slowTask.cancel(true); // interrupt the running task
            System.out.println("  Cancelled: " + slowTask.isCancelled());
        } catch (ExecutionException e) {
            System.out.println("  ExecutionException: " + e.getCause().getMessage());
        }

        // ----------------------------------------------------------
        // 4. Exception Handling — Callable throws, Future wraps it
        // ----------------------------------------------------------
        System.out.println("\n--- 4. Exception in Callable ---");

        Future<Integer> failingTask = executor.submit(() -> {
            Thread.sleep(200);
            if (true) throw new IllegalStateException("Something went wrong inside Callable!");
            return -1; // unreachable
        });

        try {
            failingTask.get();
        } catch (ExecutionException e) {
            // The real exception is wrapped inside ExecutionException
            System.out.println("  Caught ExecutionException!");
            System.out.println("  Root cause: " + e.getCause().getClass().getSimpleName()
                    + " — " + e.getCause().getMessage());
        }

        // ----------------------------------------------------------
        // 5. invokeAny — returns the FIRST successful result
        // ----------------------------------------------------------
        System.out.println("\n--- 5. invokeAny — first task to finish wins ---");

        List<Callable<String>> raceTasks = List.of(
            () -> { Thread.sleep(800); return "SlowTask finished"; },
            () -> { Thread.sleep(200); return "FastTask finished"; },
            () -> { Thread.sleep(500); return "MediumTask finished"; }
        );

        try {
            String first = executor.invokeAny(raceTasks); // blocks until first success
            System.out.println("  First result: " + first);
        } catch (ExecutionException e) {
            System.out.println("  All tasks failed: " + e.getCause());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // -------------------------------------------------------
        // KEY POINTS:
        // - Callable<V> → returns value; can throw checked exceptions
        // - Future<V>   → handle to the pending result
        // - get()       → blocks; throws ExecutionException if task failed
        // - invokeAll() → submits all; returns futures for all
        // - invokeAny() → submits all; returns first successful result
        // - cancel(true)→ interrupts the running thread if still running
        // -------------------------------------------------------
        System.out.println("\n=== Callable and Future Demo Complete ===");
    }
}

