package collectionsComplete;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionIntroDemo {
    public static void run() {
        DemoPrinter.section("1. Collection Interface Basics");

        Collection<String> languages = new ArrayList<>();
        languages.add("Java");
        languages.add("Python");
        languages.add("JavaScript");

        System.out.println("Collection contents: " + languages);
        System.out.println("Size: " + languages.size());
        System.out.println("Contains Java: " + languages.contains("Java"));

        languages.remove("Python");
        System.out.println("After removing Python: " + languages);

        System.out.println("isEmpty: " + languages.isEmpty());
        languages.clear();
        System.out.println("After clear: " + languages);
    }
}
