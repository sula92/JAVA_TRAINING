package collectionsComplete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IteratorConceptDemo {
    public static void run() {
        DemoPrinter.section("6. Iterator and ListIterator");

        List<String> modules = new ArrayList<>();
        modules.add("OOP");
        modules.add("Collections");
        modules.add("Threads");
        modules.add("Old Topic");

        Iterator<String> iterator = modules.iterator();
        while (iterator.hasNext()) {
            String module = iterator.next();
            if ("Old Topic".equals(module)) {
                iterator.remove();
            }
        }
        System.out.println("After safe remove using Iterator: " + modules);

        ListIterator<String> listIterator = modules.listIterator();
        while (listIterator.hasNext()) {
            String module = listIterator.next();
            if ("Collections".equals(module)) {
                listIterator.set("Java Collections Framework");
                listIterator.add("Generics");
            }
        }
        System.out.println("After ListIterator set/add: " + modules);

        System.out.println("Reverse traversal:");
        while (listIterator.hasPrevious()) {
            System.out.println("  " + listIterator.previous());
        }
    }
}
