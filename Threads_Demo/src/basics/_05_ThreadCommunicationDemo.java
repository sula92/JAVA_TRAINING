package basics;

import java.util.LinkedList;
import java.util.Queue;

// ============================================================
// TOPIC: Thread Communication — wait() / notify() / notifyAll()
// ============================================================
// wait()      → releases the lock; suspends the calling thread
//               until another thread calls notify()/notifyAll()
// notify()    → wakes ONE random waiting thread on this object
// notifyAll() → wakes ALL waiting threads on this object
//
// RULES:
//   - All three must be called INSIDE a synchronized block/method
//   - Always use 'while', NOT 'if', to re-check the condition
//     (guards against spurious wakeups)
//
// CLASSIC USE CASE: Producer-Consumer Pattern
//   Producer adds items to a bounded buffer.
//   Consumer removes items from the buffer.
//   They coordinate using wait/notify so neither overflows
//   nor reads from an empty buffer.
// ============================================================

public class _05_ThreadCommunicationDemo {

    // ----------------------------------------------------------
    // Shared Buffer — the object both Producer and Consumer use
    // ----------------------------------------------------------
    static class BoundedBuffer {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int CAPACITY = 3;

        // Called by Producer
        public synchronized void produce(int item) throws InterruptedException {
            // Use WHILE (not if) — guards against spurious wakeups
            while (buffer.size() == CAPACITY) {
                System.out.println("  [Producer] Buffer FULL. Waiting...");
                wait(); // releases lock; producer sleeps here
            }
            buffer.add(item);
            System.out.println("  [Producer] Produced: " + item
                    + "  | Buffer size: " + buffer.size());
            notifyAll(); // wake up any waiting consumers
        }

        // Called by Consumer
        public synchronized int consume() throws InterruptedException {
            // Use WHILE (not if)
            while (buffer.isEmpty()) {
                System.out.println("  [Consumer] Buffer EMPTY. Waiting...");
                wait(); // releases lock; consumer sleeps here
            }
            int item = buffer.poll();
            System.out.println("  [Consumer] Consumed: " + item
                    + "  | Buffer size: " + buffer.size());
            notifyAll(); // wake up any waiting producers
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Thread Communication Demo (Producer-Consumer) ===\n");

        BoundedBuffer sharedBuffer = new BoundedBuffer();

        // Producer Thread — produces items 1..6
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 6; i++) {
                try {
                    Thread.sleep(300); // simulate production time
                    sharedBuffer.produce(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("\n  [Producer] Done producing.");
        }, "Producer");

        // Consumer Thread — consumes 6 items
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                try {
                    Thread.sleep(700); // consume slower than producer → buffer will fill
                    sharedBuffer.consume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("  [Consumer] Done consuming.");
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        // ----------------------------------------------------------
        // Bonus: notifyAll() with multiple waiting threads
        // ----------------------------------------------------------
        System.out.println("\n--- notifyAll() Demo: multiple waiting threads ---");

        Object signal = new Object();

        // Create 3 "waiter" threads that all wait on 'signal'
        for (int i = 1; i <= 3; i++) {
            final int id = i;
            Thread waiter = new Thread(() -> {
                synchronized (signal) {
                    try {
                        System.out.println("  Waiter-" + id + " is waiting...");
                        signal.wait();
                        System.out.println("  Waiter-" + id + " was notified and woke up!");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, "Waiter-" + i);
            waiter.start();
        }

        Thread.sleep(500); // let all waiters enter wait()

        synchronized (signal) {
            System.out.println("\n  Notifier calling notifyAll()...");
            signal.notifyAll(); // wake ALL three waiters at once
        }

        Thread.sleep(300); // let waiters finish

        // -------------------------------------------------------
        // KEY POINTS:
        // - wait()      → must own the lock; releases lock while waiting
        // - notify()    → wakes ONE random waiter (non-deterministic)
        // - notifyAll() → wakes ALL waiters (preferred; safer)
        // - Always use  'while' loop to recheck condition after wakeup
        // - Spurious wakeup: JVM may wake a thread even without notify()
        // -------------------------------------------------------
        System.out.println("\n=== Thread Communication Demo Complete ===");
    }
}

