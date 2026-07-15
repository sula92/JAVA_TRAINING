import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueueDequeDemo {
    public static void run() {
        DemoPrinter.section("4. Queue and Deque - Processing Order");

        Queue<String> printQueue = new ArrayDeque<>();
        printQueue.offer("assignment.pdf");
        printQueue.offer("invoice.pdf");
        printQueue.offer("notes.pdf");

        System.out.println("Queue: " + printQueue);
        System.out.println("peek: " + printQueue.peek());
        System.out.println("poll: " + printQueue.poll());
        System.out.println("After poll: " + printQueue);

        ArrayDeque<String> browserHistory = new ArrayDeque<>();
        browserHistory.push("home");
        browserHistory.push("courses");
        browserHistory.push("collections lesson");
        System.out.println("Stack behavior using ArrayDeque: " + browserHistory);
        System.out.println("Back button pops: " + browserHistory.pop());

        Queue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.offer(30);
        priorityQueue.offer(10);
        priorityQueue.offer(20);
        System.out.println("PriorityQueue processes smallest first:");
        while (!priorityQueue.isEmpty()) {
            System.out.println("  " + priorityQueue.poll());
        }
    }
}
