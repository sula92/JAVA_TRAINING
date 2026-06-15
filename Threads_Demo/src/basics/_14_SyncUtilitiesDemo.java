package basics;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

// ============================================================
// TOPIC: Synchronization Utilities & Atomic Classes
// ============================================================
// CountDownLatch:
//   - One or more threads WAIT until a counter reaches 0
//   - One-shot: cannot be reset after it reaches 0
//   - Use: wait for N services/events before proceeding
//
// CyclicBarrier:
//   - N threads all WAIT at a barrier point; when all arrive they proceed
//   - Re-usable (can be used for multiple rounds/phases)
//   - Optional "barrier action" runs when the barrier trips
//
// Semaphore:
//   - Controls access to a pool of N resources (permits)
//   - acquire() → take a permit (blocks if none available)
//   - release() → return a permit
//   - Use: connection pools, rate limiters, parking lots
//
// Atomic Classes (java.util.concurrent.atomic):
//   - Lock-free, thread-safe via CPU CAS (Compare-And-Swap)
//   - AtomicBoolean, AtomicInteger, AtomicLong, AtomicReference
//   - Faster than synchronized for single-variable operations
// ============================================================

public class _14_SyncUtilitiesDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Synchronization Utilities & Atomic Classes Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: CountDownLatch — wait for N workers to finish
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: CountDownLatch ---");

        int workerCount = 3;
        CountDownLatch latch = new CountDownLatch(workerCount);

        for (int i = 1; i <= workerCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("  Worker-" + id + " started");
                    Thread.sleep(id * 300L); // simulate varying durations
                    System.out.println("  Worker-" + id + " done → countDown()");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // decrement the counter
                }
            }, "Worker-" + id).start();
        }

        System.out.println("  [Main] Waiting for all workers...");
        latch.await(); // blocks until count == 0
        System.out.println("  [Main] All workers finished! Proceeding.\n");

        // CountDownLatch with timeout
        CountDownLatch timedLatch = new CountDownLatch(1);
        boolean reached = timedLatch.await(500, TimeUnit.MILLISECONDS); // never counted down
        System.out.println("  Timed await reached: " + reached + " (false = timed out)\n");

        // ----------------------------------------------------------
        // Demo 2: CyclicBarrier — all threads meet at a checkpoint
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: CyclicBarrier ---");

        int parties = 3;
        // Optional Runnable runs when all parties arrive at the barrier
        CyclicBarrier barrier = new CyclicBarrier(parties,
            () -> System.out.println("  *** All threads reached the barrier — proceeding! ***"));

        for (int round = 1; round <= 2; round++) { // demonstrate re-use
            System.out.println("  [Round " + round + "]");
            Thread[] phaseThreads = new Thread[parties];
            for (int i = 0; i < parties; i++) {
                final int id = i + 1;
                phaseThreads[i] = new Thread(() -> {
                    try {
                        System.out.println("  Thread-" + id + " doing phase work...");
                        Thread.sleep(id * 200L);
                        System.out.println("  Thread-" + id + " reached barrier → await()");
                        barrier.await(); // wait for everyone
                        System.out.println("  Thread-" + id + " passed the barrier");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }, "BarrierThread-" + id);
                phaseThreads[i].start();
            }
            for (Thread t : phaseThreads) t.join();
            System.out.println();
        }

        // ----------------------------------------------------------
        // Demo 3: Semaphore — limit concurrent access to a resource
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Semaphore (max 2 concurrent) ---");

        Semaphore semaphore = new Semaphore(2); // only 2 threads at a time

        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    semaphore.acquire(); // blocks if no permits available
                    System.out.println("  [Task-" + id + "] Acquired permit. Active: "
                            + (2 - semaphore.availablePermits()));
                    Thread.sleep(400); // simulate resource use
                    System.out.println("  [Task-" + id + "] Releasing permit.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release(); // ALWAYS in finally
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("  Available permits after all done: " + semaphore.availablePermits() + "\n");

        // ----------------------------------------------------------
        // Demo 4: Atomic Classes — lock-free thread-safe operations
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Atomic Classes ---");

        AtomicInteger atomicInt       = new AtomicInteger(0);
        AtomicLong    atomicLong      = new AtomicLong(100L);
        AtomicBoolean atomicBoolean   = new AtomicBoolean(false);
        AtomicReference<String> atomicRef = new AtomicReference<>("initial");

        // AtomicInteger operations
        System.out.println("  incrementAndGet : " + atomicInt.incrementAndGet());  // 1
        System.out.println("  getAndIncrement : " + atomicInt.getAndIncrement());  // 1 (post-increment)
        System.out.println("  addAndGet(5)     : " + atomicInt.addAndGet(5));       // 7
        System.out.println("  compareAndSet(7, 10): "
                + atomicInt.compareAndSet(7, 10));                                  // true
        System.out.println("  After CAS, value: " + atomicInt.get());              // 10

        System.out.println("  AtomicLong  addAndGet(50): " + atomicLong.addAndGet(50));    // 150
        System.out.println("  AtomicBoolean getAndSet(true): " + atomicBoolean.getAndSet(true)); // false
        System.out.println("  AtomicBoolean now: " + atomicBoolean.get());                // true

        atomicRef.compareAndSet("initial", "updated");
        System.out.println("  AtomicReference after CAS: " + atomicRef.get());     // updated

        // AtomicInteger as a thread-safe counter (already shown in _09, but quick recap)
        AtomicInteger counter = new AtomicInteger(0);
        Thread[] atomicThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) counter.incrementAndGet();
            });
        }
        for (Thread t : atomicThreads) t.start();
        for (Thread t : atomicThreads) t.join();
        System.out.println("\n  AtomicInteger counter (5 threads × 1000): " + counter.get()
                + " correct=" + (counter.get() == 5000));

        // ----------------------------------------------------------
        // Demo 5: Exchanger — two threads swap data at a meeting point
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 5: Exchanger ---");

        Exchanger<String> exchanger = new Exchanger<>();

        Thread thread1 = new Thread(() -> {
            try {
                String data = "Hello from Thread-1";
                System.out.println("  Thread-1 sending  : " + data);
                String received = exchanger.exchange(data);
                System.out.println("  Thread-1 received : " + received);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            try {
                String data = "Hello from Thread-2";
                System.out.println("  Thread-2 sending  : " + data);
                String received = exchanger.exchange(data);
                System.out.println("  Thread-2 received : " + received);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Thread-2");

        thread1.start(); thread2.start();
        thread1.join();  thread2.join();

        // -------------------------------------------------------
        // KEY POINTS:
        // - CountDownLatch: one-shot; await() blocks; countDown() decrements
        // - CyclicBarrier : reusable; all parties must call await()
        // - Semaphore     : N permits; acquire()/release() in try/finally
        // - Atomic*       : lock-free CAS; faster than synchronized for
        //                   single-variable updates
        // - Exchanger     : exactly 2 threads swap objects at a rendezvous
        // -------------------------------------------------------
        System.out.println("\n=== Sync Utilities & Atomic Classes Demo Complete ===");
    }
}

