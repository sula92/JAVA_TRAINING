package basics;

import java.util.concurrent.locks.*;
import java.util.HashMap;
import java.util.Map;

// ============================================================
// TOPIC: java.util.concurrent Locks
// ============================================================
// ReentrantLock — more powerful than 'synchronized':
//   lock()              → acquire the lock (blocks if busy)
//   unlock()            → release the lock (ALWAYS in finally!)
//   tryLock()           → non-blocking; returns true if acquired
//   tryLock(t, unit)    → timed attempt; returns false on timeout
//   lockInterruptibly() → can be interrupted while waiting
//   isHeldByCurrentThread() → true if this thread holds the lock
//   newCondition()      → Condition for wait/signal
//
// ReentrantReadWriteLock — optimised for read-heavy workloads:
//   readLock()  → multiple threads can hold simultaneously
//   writeLock() → exclusive; no readers or other writers allowed
//
// Fairness mode: new ReentrantLock(true)
//   → longest-waiting thread gets the lock first (lower throughput)
// ============================================================

public class _12_LocksDemo {

    // ----------------------------------------------------------
    // Demo 1: ReentrantLock — basic usage
    // ----------------------------------------------------------
    static class BankAccount {
        private double balance;
        private final ReentrantLock lock = new ReentrantLock();

        BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        public void deposit(double amount) {
            lock.lock();
            try {
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] Depositing " + amount);
                balance += amount;
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] Balance after deposit: " + balance);
            } finally {
                lock.unlock(); // MUST be in finally — always released
            }
        }

        public boolean tryWithdraw(double amount) {
            if (lock.tryLock()) { // non-blocking attempt
                try {
                    if (balance >= amount) {
                        balance -= amount;
                        System.out.println("  [" + Thread.currentThread().getName()
                                + "] Withdrew " + amount + " | Balance: " + balance);
                        return true;
                    } else {
                        System.out.println("  [" + Thread.currentThread().getName()
                                + "] Insufficient funds");
                        return false;
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] Could not acquire lock — skipping withdrawal");
                return false;
            }
        }

        public double getBalance() { return balance; }
    }

    // ----------------------------------------------------------
    // Demo 2: ReentrantReadWriteLock — shared reads, exclusive writes
    // ----------------------------------------------------------
    static class SharedCache {
        private final Map<String, String> cache = new HashMap<>();
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock readLock  = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();

        public void put(String key, String value) {
            writeLock.lock(); // exclusive — all readers must wait
            try {
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] WRITING: " + key + "=" + value);
                cache.put(key, value);
                Thread.sleep(100); // simulate slow write
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock();
            }
        }

        public String get(String key) {
            readLock.lock(); // shared — multiple readers at once
            try {
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] READING: " + key + " → " + cache.get(key));
                Thread.sleep(50); // simulate slow read
                return cache.get(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                readLock.unlock();
            }
        }
    }

    // ----------------------------------------------------------
    // Demo 3: Condition — finer-grained wait/signal than object monitors
    // ----------------------------------------------------------
    static class BoundedStack {
        private final int[] data = new int[3];
        private int top = -1;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notFull  = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        public void push(int val) throws InterruptedException {
            lock.lock();
            try {
                while (top == data.length - 1) {
                    System.out.println("  [" + Thread.currentThread().getName() + "] Stack FULL — waiting");
                    notFull.await();
                }
                data[++top] = val;
                System.out.println("  [" + Thread.currentThread().getName() + "] Pushed: " + val);
                notEmpty.signal();
            } finally { lock.unlock(); }
        }

        public int pop() throws InterruptedException {
            lock.lock();
            try {
                while (top == -1) {
                    System.out.println("  [" + Thread.currentThread().getName() + "] Stack EMPTY — waiting");
                    notEmpty.await();
                }
                int val = data[top--];
                System.out.println("  [" + Thread.currentThread().getName() + "] Popped: " + val);
                notFull.signal();
                return val;
            } finally { lock.unlock(); }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Locks Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: ReentrantLock basic usage
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: ReentrantLock ---");
        BankAccount account = new BankAccount(1000);

        Thread depositor = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.deposit(100);
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Depositor");

        Thread withdrawer = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.tryWithdraw(200);
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Withdrawer");

        depositor.start(); withdrawer.start();
        depositor.join();  withdrawer.join();
        System.out.println("  Final balance: " + account.getBalance() + "\n");

        // ----------------------------------------------------------
        // Demo 2: ReentrantReadWriteLock
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: ReentrantReadWriteLock ---");
        SharedCache sharedCache = new SharedCache();

        // One writer writes initial data
        Thread writer = new Thread(() -> {
            sharedCache.put("user:1", "Alice");
            sharedCache.put("user:2", "Bob");
        }, "Writer");

        writer.start();
        writer.join();

        // Multiple readers can read simultaneously
        Thread[] readers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int id = i + 1;
            readers[i] = new Thread(() -> {
                sharedCache.get("user:" + id);
            }, "Reader-" + id);
        }
        for (Thread r : readers) r.start();
        for (Thread r : readers) r.join();
        System.out.println("  (All 3 readers ran concurrently under readLock)\n");

        // ----------------------------------------------------------
        // Demo 3: Condition (advanced wait/signal)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Condition (newCondition) ---");
        BoundedStack stack = new BoundedStack();

        Thread pusher = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    stack.push(i * 10);
                    Thread.sleep(150);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Pusher");

        Thread popper = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    stack.pop();
                    Thread.sleep(300);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Popper");

        pusher.start(); popper.start();
        pusher.join();  popper.join();

        // -------------------------------------------------------
        // KEY POINTS:
        // - Always unlock() in a finally block
        // - tryLock() is non-blocking — good for lock-free fallbacks
        // - ReadWriteLock: many readers OR one writer (never both)
        // - Condition.await() = Object.wait(); signal() = notify()
        //   but Condition is tied to a specific ReentrantLock
        // - Fair lock (new ReentrantLock(true)) prevents starvation
        //   at the cost of lower throughput
        // -------------------------------------------------------
        System.out.println("\n=== Locks Demo Complete ===");
    }
}

