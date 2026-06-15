package basics;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// ============================================================
// TOPIC: Concurrent Collections
// ============================================================
// Regular collections (ArrayList, HashMap) are NOT thread-safe.
// java.util.concurrent provides safe, high-performance alternatives.
//
// ConcurrentHashMap:
//   - Thread-safe HashMap; uses bucket-level locking (not full lock)
//   - Much better throughput than Collections.synchronizedMap()
//   - computeIfAbsent(), merge(), putIfAbsent() are also atomic
//
// CopyOnWriteArrayList:
//   - Thread-safe ArrayList; writes create a NEW copy of the array
//   - Reads NEVER block (always read a stable snapshot)
//   - Best when reads >> writes (listener lists, subscriber lists)
//
// BlockingQueue (ArrayBlockingQueue / LinkedBlockingQueue):
//   - put()   → BLOCKS if full (unlike offer())
//   - take()  → BLOCKS if empty (unlike poll())
//   - Perfect for producer-consumer without manual wait/notify
// ============================================================

public class _13_ConcurrentCollectionsDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Concurrent Collections Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: ConcurrentHashMap
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: ConcurrentHashMap ---");

        ConcurrentHashMap<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();

        String[] words = {"apple", "banana", "apple", "cherry", "banana", "apple"};

        // Multiple threads count words concurrently — safe with ConcurrentHashMap

        // Use a pool for cleaner concurrent access
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (String word : words) {
            pool.submit(() -> {
                // computeIfAbsent + incrementAndGet is thread-safe
                wordCount.computeIfAbsent(word, k -> new AtomicInteger(0))
                         .incrementAndGet();
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("  Word counts: " + wordCount);

        // putIfAbsent — atomic check-then-put
        wordCount.putIfAbsent("durian", new AtomicInteger(1));
        System.out.println("  After putIfAbsent(durian): " + wordCount.get("durian").get() + "\n");

        // ----------------------------------------------------------
        // Demo 2: CopyOnWriteArrayList
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: CopyOnWriteArrayList ---");

        CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();
        listeners.add("Listener-1");
        listeners.add("Listener-2");
        listeners.add("Listener-3");

        // Readers iterate the list; writers add/remove concurrently
        Thread reader = new Thread(() -> {
            for (String l : listeners) { // reads a snapshot — safe even during writes
                System.out.println("  [Reader] " + l);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Reader");

        Thread writer = new Thread(() -> {
            listeners.add("Listener-4"); // creates a new internal copy
            System.out.println("  [Writer] Added Listener-4");
            listeners.remove("Listener-1");
            System.out.println("  [Writer] Removed Listener-1");
        }, "Writer");

        reader.start();
        Thread.sleep(50); // let reader start first
        writer.start();

        reader.join();
        writer.join();

        System.out.println("  Final list: " + listeners + "\n");

        // ----------------------------------------------------------
        // Demo 3: ArrayBlockingQueue (bounded, FIFO blocking queue)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: ArrayBlockingQueue (Producer-Consumer) ---");

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3); // capacity 3

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 6; i++) {
                try {
                    System.out.println("  [Producer] putting " + i
                            + "  | queue size: " + queue.size());
                    queue.put(i);    // BLOCKS if queue is full
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("  [Producer] done.");
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                try {
                    Thread.sleep(500);
                    int val = queue.take(); // BLOCKS if queue is empty
                    System.out.println("  [Consumer] took " + val
                            + "  | queue size: " + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("  [Consumer] done.");
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // ----------------------------------------------------------
        // Demo 4: Non-blocking offer() and poll()
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 4: Non-blocking offer() and poll() ---");

        BlockingQueue<String> bq = new ArrayBlockingQueue<>(2);
        System.out.println("  offer(A): " + bq.offer("A"));  // true
        System.out.println("  offer(B): " + bq.offer("B"));  // true
        System.out.println("  offer(C): " + bq.offer("C"));  // false — full!
        System.out.println("  poll()  : " + bq.poll());      // A
        System.out.println("  poll()  : " + bq.poll());      // B
        System.out.println("  poll()  : " + bq.poll());      // null — empty!

        // -------------------------------------------------------
        // KEY POINTS:
        // - ConcurrentHashMap: no full-map lock; high throughput
        // - CopyOnWriteArrayList: writes are expensive (copy); reads are free
        // - BlockingQueue: put/take block; offer/poll don't → choose wisely
        // - These classes eliminate the need for manual synchronized blocks
        //   around collection access, reducing risk of bugs
        // -------------------------------------------------------
        System.out.println("\n=== Concurrent Collections Demo Complete ===");
    }
}

