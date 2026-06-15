package collectionsComplete;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapConceptDemo {
    public static void run() {
        DemoPrinter.section("5. Map - Key Value Pairs");

        Map<Integer, String> studentNames = new HashMap<>();
        studentNames.put(101, "Kamal");
        studentNames.put(102, "Nimal");
        studentNames.put(103, "Amali");
        studentNames.put(102, "Nimal Perera");

        System.out.println("HashMap: " + studentNames);
        System.out.println("Get id 102: " + studentNames.get(102));
        System.out.println("Contains key 101: " + studentNames.containsKey(101));
        System.out.println("Contains value Amali: " + studentNames.containsValue("Amali"));

        studentNames.putIfAbsent(104, "Saman");
        studentNames.remove(103);
        System.out.println("After putIfAbsent and remove: " + studentNames);

        Map<String, Product> products = new LinkedHashMap<>();
        products.put("P100", new Product("P100", "Laptop", 950.0));
        products.put("P200", new Product("P200", "Hard Disk", 80.0));
        products.put("P300", new Product("P300", "Mouse", 15.0));

        System.out.println("LinkedHashMap keeps insertion order:");
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        Map<Integer, String> sortedByKey = new TreeMap<>(studentNames);
        System.out.println("TreeMap sorts by key: " + sortedByKey);
    }
}
