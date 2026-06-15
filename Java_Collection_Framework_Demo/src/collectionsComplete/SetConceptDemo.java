package collectionsComplete;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class SetConceptDemo {
    public static void run() {
        DemoPrinter.section("3. Set - Unique Elements");

        Set<String> hashSet = new HashSet<>();
        hashSet.add("Java");
        hashSet.add("Python");
        hashSet.add("Java");
        hashSet.add("C#");
        System.out.println("HashSet removes duplicates, order not guaranteed: " + hashSet);

        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Java");
        linkedHashSet.add("Python");
        linkedHashSet.add("Java");
        linkedHashSet.add("C#");
        System.out.println("LinkedHashSet keeps insertion order: " + linkedHashSet);

        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Java");
        treeSet.add("Python");
        treeSet.add("C#");
        treeSet.add("Kotlin");
        System.out.println("TreeSet keeps sorted order: " + treeSet);

        Set<Student> students = new HashSet<>();
        students.add(new Student(1, "Nimal", 3.5));
        students.add(new Student(1, "Nimal duplicate", 3.7));
        students.add(new Student(2, "Akash", 3.8));
        System.out.println("Student Set uses equals/hashCode by id: " + students);
    }
}
