package collectionsComplete;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListConceptDemo {
    public static void run() {
        DemoPrinter.section("2. List - Ordered and Allows Duplicates");

        List<String> tasks = new ArrayList<>();
        tasks.add("Wake up");
        tasks.add("Study Java");
        tasks.add("Practice code");
        tasks.add("Study Java");
        tasks.add(1, "Drink water");

        System.out.println("ArrayList tasks: " + tasks);
        System.out.println("Element at index 2: " + tasks.get(2));
        System.out.println("Index of Study Java: " + tasks.indexOf("Study Java"));

        tasks.set(2, "Study Java collections");
        System.out.println("After set: " + tasks);

        List<String> recentFiles = new LinkedList<>();
        recentFiles.add("notes.md");
        recentFiles.add("demo.java");
        recentFiles.add("summary.txt");

        System.out.println("LinkedList recent files: " + recentFiles);
        System.out.println("List is best when order and index access matter.");
    }
}
