package basics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// ============================================================
// TOPIC: ThreadLocal
// ============================================================
// ThreadLocal<T> gives each thread its OWN independent copy of
// a variable. No sharing → no synchronization needed.
//
// API:
//   ThreadLocal.withInitial(() -> initialValue)  → lazy initial value
//   tl.get()     → get this thread's value
//   tl.set(val)  → set this thread's value
//   tl.remove()  → CRITICAL: remove value to prevent memory leaks
//                   in thread pools (threads are reused!)
//
// Common use cases:
//   - SimpleDateFormat (not thread-safe → give each thread its own)
//   - Per-thread counters or IDs
//   - User/request context in web servers
//   - Spring @Transactional (binds DB connection per thread)
// ============================================================

public class _10_ThreadLocalDemo {

    // ----------------------------------------------------------
    // Demo 1: Per-thread counter — each thread has its own count
    // ----------------------------------------------------------
    static ThreadLocal<Integer> threadLocalCounter =
            ThreadLocal.withInitial(() -> 0);

    // ----------------------------------------------------------
    // Demo 2: Per-thread SimpleDateFormat
    // SimpleDateFormat is NOT thread-safe; ThreadLocal gives each
    // thread its own instance without synchronization overhead.
    // ----------------------------------------------------------
    static ThreadLocal<SimpleDateFormat> dateFormatLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    // ----------------------------------------------------------
    // Demo 3: Request context simulation
    // In a web server, each request runs in a thread. ThreadLocal
    // lets us store the current user without passing it everywhere.
    // ----------------------------------------------------------
    static ThreadLocal<String> currentUser = new ThreadLocal<>();

    static void processRequest(String username) {
        currentUser.set(username); // store for this thread
        try {
            System.out.println("  [" + Thread.currentThread().getName()
                    + "] Processing request for: " + currentUser.get());
            doBusinessLogic();
        } finally {
            currentUser.remove(); // ALWAYS clean up in thread pools!
        }
    }

    static void doBusinessLogic() {
        // Deep in the call stack — no need to pass username as parameter
        System.out.println("  [" + Thread.currentThread().getName()
                + "]   Business logic: user = " + currentUser.get());
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== ThreadLocal Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Per-thread counter
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Per-thread counter ---");

        Runnable counterTask = () -> {
            for (int i = 0; i < 3; i++) {
                // Each thread increments ITS OWN copy — no race condition
                threadLocalCounter.set(threadLocalCounter.get() + 1);
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] counter = " + threadLocalCounter.get());
            }
            threadLocalCounter.remove(); // clean up
        };

        Thread ta = new Thread(counterTask, "Thread-A");
        Thread tb = new Thread(counterTask, "Thread-B");
        ta.start(); tb.start();
        ta.join();  tb.join();
        System.out.println("  (Notice: each thread's counter is independent)\n");

        // ----------------------------------------------------------
        // Demo 2: Thread-safe SimpleDateFormat via ThreadLocal
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Per-thread SimpleDateFormat ---");

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                // Each thread has its OWN SimpleDateFormat — no synchronization
                String formatted = dateFormatLocal.get().format(new Date());
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] Formatted date: " + formatted);
                dateFormatLocal.remove(); // clean up after use in pool
            });
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Request context (simulate web server threads)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Request context simulation ---");

        ExecutorService webPool = Executors.newFixedThreadPool(2);

        String[] users = {"Alice", "Bob", "Charlie", "Diana"};
        for (String user : users) {
            webPool.submit(() -> processRequest(user));
        }

        webPool.shutdown();
        webPool.awaitTermination(5, TimeUnit.SECONDS);

        // ----------------------------------------------------------
        // Demo 4: Verify isolation — each thread sees ONLY its own value
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 4: Isolation verification ---");

        ThreadLocal<String> tl = new ThreadLocal<>();

        Thread t1 = new Thread(() -> {
            tl.set("Value from Thread-1");
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("  Thread-1 reads: " + tl.get()); // "Value from Thread-1"
            tl.remove();
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            tl.set("Value from Thread-2");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("  Thread-2 reads: " + tl.get()); // "Value from Thread-2"
            tl.remove();
        }, "Thread-2");

        t1.start(); t2.start();
        t1.join();  t2.join();
        System.out.println("  Main reads: " + tl.get()); // null — main never set a value

        // -------------------------------------------------------
        // KEY POINTS:
        // - ThreadLocal gives each thread its own variable copy
        // - No synchronization needed → better performance
        // - ALWAYS call remove() in thread pools to prevent leaks
        //   (threads are reused; stale values would persist)
        // - Useful for: per-request context, non-thread-safe objects,
        //   per-thread caches
        // -------------------------------------------------------
        System.out.println("\n=== ThreadLocal Demo Complete ===");
    }
}

