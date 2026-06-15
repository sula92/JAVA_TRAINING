package lambdaDemo;

import java.util.*;

// ============================================================
// TOPIC: Comparator with Lambdas and Method References
// ============================================================
// Comparator<T> is a @FunctionalInterface:
//   int compare(T o1, T o2)
//
// It is the most common target for lambda expressions and
// method references in everyday Java code.
//
// Key static/default methods:
//   Comparator.comparing(keyExtractor)             → sort by a key
//   Comparator.comparingInt/Double/Long(extractor) → avoids boxing
//   Comparator.naturalOrder()                      → natural ordering
//   Comparator.reverseOrder()                      → reverse natural order
//   comparator.reversed()                          → reverse any comparator
//   comparator.thenComparing(other)                → secondary sort
//   Comparator.nullsFirst/nullsLast(comparator)    → null-safe comparator
// ============================================================

public class _10_ComparatorDemo {

    static class Student {
        String name;
        String major;
        double gpa;
        int    year;

        Student(String name, String major, double gpa, int year) {
            this.name  = name;
            this.major = major;
            this.gpa   = gpa;
            this.year  = year;
        }

        String getName()  { return name; }
        String getMajor() { return major; }
        double getGpa()   { return gpa; }
        int    getYear()  { return year; }

        @Override
        public String toString() {
            return String.format("%-10s %-12s GPA=%.1f Year=%d", name, major, gpa, year);
        }
    }

    static void printList(String label, List<Student> list) {
        System.out.println("  " + label + ":");
        list.forEach(s -> System.out.println("    " + s));
        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println("=== Comparator with Lambdas & Method References Demo ===\n");

        List<Student> students = Arrays.asList(
            new Student("Alice",   "CS",      3.9, 3),
            new Student("Bob",     "Math",    3.5, 2),
            new Student("Charlie", "CS",      3.7, 1),
            new Student("Diana",   "Physics", 3.9, 4),
            new Student("Eve",     "Math",    3.3, 3),
            new Student("Frank",   "CS",      3.7, 2)
        );

        // ----------------------------------------------------------
        // Demo 1: Old way — anonymous class
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Old Way (Anonymous Class) ---");

        List<Student> copy = new ArrayList<>(students);
        copy.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return a.getName().compareTo(b.getName());
            }
        });
        printList("Sorted by name (anonymous class)", copy);

        // ----------------------------------------------------------
        // Demo 2: Lambda expression
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Lambda Expression ---");

        copy = new ArrayList<>(students);
        copy.sort((a, b) -> a.getName().compareTo(b.getName()));
        printList("Sorted by name (lambda)", copy);

        // ----------------------------------------------------------
        // Demo 3: Method reference (arbitrary instance)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Method Reference ---");

        copy = new ArrayList<>(students);
        copy.sort(Comparator.comparing(Student::getName));  // cleanest form
        printList("Sorted by name (method reference)", copy);

        // ----------------------------------------------------------
        // Demo 4: Comparator.comparing() with key extractor
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Comparator.comparing() ---");

        List<String> words = Arrays.asList("banana", "apple", "cherry", "date", "elderberry");

        // Sort by length
        words.sort(Comparator.comparingInt(String::length));
        System.out.println("  By length (asc) : " + words);

        // Sort by length descending
        words.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("  By length (desc): " + words);

        // Sort alphabetically
        words.sort(Comparator.naturalOrder());
        System.out.println("  Natural order   : " + words);

        // Sort reverse alphabetically
        words.sort(Comparator.reverseOrder());
        System.out.println("  Reverse order   : " + words + "\n");

        // ----------------------------------------------------------
        // Demo 5: Multi-level sort with thenComparing()
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Multi-Level Sort with thenComparing() ---");

        copy = new ArrayList<>(students);

        // Sort by major, then by GPA descending, then by name
        copy.sort(
            Comparator.comparing(Student::getMajor)
                      .thenComparingDouble(Student::getGpa).reversed()
                      // reversed() reverses ALL levels — not ideal
        );
        // Better approach: build levels individually
        Comparator<Student> byMajor = Comparator.comparing(Student::getMajor);
        Comparator<Student> byGpaDesc = Comparator.comparingDouble(Student::getGpa).reversed();
        Comparator<Student> byName  = Comparator.comparing(Student::getName);

        copy.sort(byMajor.thenComparing(byGpaDesc).thenComparing(byName));
        printList("Sorted by major → GPA(desc) → name", copy);

        // ----------------------------------------------------------
        // Demo 6: Sorting with Comparator.comparing() + lambda key
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Custom Key Extractor with Lambda ---");

        copy = new ArrayList<>(students);

        // Sort by name length, then alphabetically
        copy.sort(Comparator.comparingInt((Student s) -> s.getName().length())
                             .thenComparing(Student::getName));
        printList("Sorted by name-length then name", copy);

        // Sort by last character of name
        copy.sort(Comparator.comparing(s -> s.getName().charAt(s.getName().length() - 1)));
        printList("Sorted by last char of name", copy);

        // ----------------------------------------------------------
        // Demo 7: Null-safe comparator
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Null-Safe Comparator ---");

        List<String> withNulls = Arrays.asList("banana", null, "apple", null, "cherry");

        // Without null handling → NullPointerException
        // withNulls.sort(Comparator.naturalOrder()); // ❌ throws NPE

        // nullsFirst: nulls come before non-nulls
        withNulls.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("  nullsFirst: " + withNulls);

        // nullsLast: nulls come after non-nulls
        withNulls.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("  nullsLast:  " + withNulls + "\n");

        // ----------------------------------------------------------
        // Demo 8: Using Comparator with Collections.sort and TreeSet
        // ----------------------------------------------------------
        System.out.println("--- Demo 8: Comparator with TreeSet & Collections.sort ---");

        // TreeSet with custom comparator
        TreeSet<Student> sortedSet = new TreeSet<>(Comparator.comparing(Student::getGpa).reversed()
                                                              .thenComparing(Student::getName));
        sortedSet.addAll(students);

        System.out.println("  TreeSet (by GPA desc, then name):");
        sortedSet.forEach(s -> System.out.println("    " + s));
        System.out.println();

        // Collections.sort (same as List.sort)
        List<String> fruits = new ArrayList<>(Arrays.asList("mango", "apple", "banana", "kiwi"));
        Collections.sort(fruits, String::compareTo);   // method reference as Comparator
        System.out.println("  Collections.sort with String::compareTo: " + fruits);

        // ----------------------------------------------------------
        // Demo 9: Comparator.comparing() with three-level sort summary
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 9: Summary — All Four Equivalent Forms ---");

        List<Student> s4 = new ArrayList<>(students);

        // Form 1: anonymous class
        s4.sort(new Comparator<Student>() {
            @Override public int compare(Student a, Student b) {
                return Double.compare(b.getGpa(), a.getGpa()); // descending
            }
        });
        System.out.println("  Form 1 (anon class)  — first: " + s4.get(0).name);

        // Form 2: lambda
        s4.sort((a, b) -> Double.compare(b.getGpa(), a.getGpa()));
        System.out.println("  Form 2 (lambda)      — first: " + s4.get(0).name);

        // Form 3: method reference with reversed()
        s4.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        System.out.println("  Form 3 (method ref)  — first: " + s4.get(0).name);

        // Form 4: Comparator.comparing with explicit comparator
        s4.sort(Comparator.comparing(Student::getGpa, Comparator.reverseOrder()));
        System.out.println("  Form 4 (comparing)   — first: " + s4.get(0).name);

        // -------------------------------------------------------
        // KEY POINTS:
        // - Comparator is a @FunctionalInterface → accepts lambdas/method refs
        // - Comparator.comparing(keyExtractor)  → cleanest, most readable
        // - .reversed()         → flip the sort order
        // - .thenComparing()    → secondary, tertiary sorting
        // - nullsFirst/nullsLast → wrap any comparator to handle nulls
        // - Method reference   → often cleanest: Comparator.comparing(MyClass::field)
        // - TreeSet/TreeMap, Collections.sort all accept Comparator
        // -------------------------------------------------------
        System.out.println("\n=== Comparator Demo Complete ===");
    }
}

