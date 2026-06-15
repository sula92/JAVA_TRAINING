package collectionsComplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class ImmutableAndFailFastDemo {
    public static void run() {
        DemoPrinter.section("10. Immutable Collections and Fail-Fast Behavior");

        List<String> fixedSizeList = Arrays.asList("A", "B", "C");
        fixedSizeList.set(0, "Updated A");
        System.out.println("Arrays.asList allows set but not add/remove: " + fixedSizeList);

        List<String> immutableList = List.of("Java", "Collections", "Framework");
        System.out.println("List.of creates immutable list: " + immutableList);

        try {
            immutableList.add("New value");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot add to immutable List.of collection.");
        }

        List<String> topics = new ArrayList<>();
        topics.add("List");
        topics.add("Set");
        topics.add("Map");

        try {
            for (String topic : topics) {
                if ("List".equals(topic)) {
                    topics.remove(topic);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Enhanced for loop can fail when collection is modified directly.");
        }

        topics.clear();
        topics.add("List");
        topics.add("Set");
        topics.add("Map");

        Iterator<String> iterator = topics.iterator();
        while (iterator.hasNext()) {
            String topic = iterator.next();
            if ("Set".equals(topic)) {
                iterator.remove();
            }
        }
        System.out.println("Safe removal with Iterator: " + topics);
    }
}
