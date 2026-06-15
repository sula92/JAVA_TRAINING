package lambdaDemo;

import java.util.function.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// ============================================================
// TOPIC: Method References — Type 4: Constructor Reference
// ============================================================
// Syntax:  ClassName::new
//
// A constructor reference replaces a lambda that does nothing
// but call a constructor.
//
// Pattern:
//   Lambda:               ()  -> new ClassName()
//   Constructor reference: ClassName::new
//
//   Lambda:               (x) -> new ClassName(x)
//   Constructor reference: ClassName::new
//
// The compiler picks the RIGHT constructor based on the
// functional interface signature.
//
// Array constructor reference:
//   Lambda:               n -> new int[n]
//   Constructor reference: int[]::new
// ============================================================

public class _07_MethodReferences_Constructor {

    // ----------------------------------------------------------
    // Supporting classes with various constructors
    // ----------------------------------------------------------

    static class Person {
        String name;
        int    age;

        Person() {
            this.name = "Unknown";
            this.age  = 0;
        }

        Person(String name) {
            this.name = name;
            this.age  = 0;
        }

        Person(String name, int age) {
            this.name = name;
            this.age  = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    static class Box<T> {
        private T value;
        Box(T value) { this.value = value; }
        T getValue()  { return value; }
        @Override public String toString() { return "Box[" + value + "]"; }
    }

    @FunctionalInterface
    interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    public static void main(String[] args) {

        System.out.println("=== Method References — Type 4: Constructor ===\n");

        // ----------------------------------------------------------
        // Demo 1: No-arg constructor reference (Supplier)
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: No-Arg Constructor Reference ---");

        // Lambda form
        Supplier<Person>    personLambda   = () -> new Person();
        Supplier<ArrayList> listLambda     = () -> new ArrayList<>();

        // Constructor reference form
        Supplier<Person>    personRef      = Person::new;
        Supplier<ArrayList> listRef        = ArrayList::new;

        Person p1 = personLambda.get();
        Person p2 = personRef.get();

        System.out.println("  Lambda:        " + p1);
        System.out.println("  Constructor ref: " + p2);
        System.out.println("  ArrayList from ref: " + listRef.get().getClass().getSimpleName());
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: One-arg constructor reference (Function)
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: One-Arg Constructor Reference ---");

        // Lambda:           (String name) -> new Person(name)
        // Constructor ref:  Person::new  — compiler picks Person(String) constructor

        Function<String, Person>  personFromName  = Person::new;
        Function<String, Box<String>> boxFactory  = Box::new;
        Function<Integer, int[]>   intArrayFactory = int[]::new;

        Person alice = personFromName.apply("Alice");
        Person bob   = personFromName.apply("Bob");
        System.out.println("  personFromName(\"Alice\") = " + alice);
        System.out.println("  personFromName(\"Bob\")   = " + bob);
        System.out.println("  boxFactory(\"Java\")      = " + boxFactory.apply("Java"));

        // int[]::new — creates an array of the given size
        int[] arr = intArrayFactory.apply(5);
        System.out.println("  int[]::new (size 5): length = " + arr.length);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Two-arg constructor reference (BiFunction)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Two-Arg Constructor Reference ---");

        // Lambda:           (String name, Integer age) -> new Person(name, age)
        // Constructor ref:  Person::new  — compiler picks Person(String, int) constructor

        BiFunction<String, Integer, Person> personFull = Person::new;

        Person charlie = personFull.apply("Charlie", 30);
        Person diana   = personFull.apply("Diana",   25);

        System.out.println("  personFull(\"Charlie\", 30) = " + charlie);
        System.out.println("  personFull(\"Diana\",   25) = " + diana);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Constructor reference in stream operations
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Constructor Reference in Stream ---");

        List<String> names = Arrays.asList("Emma", "Frank", "Grace", "Henry");

        // Convert list of names to list of Person objects
        List<Person> people = names.stream()
            .map(Person::new)    // Function<String, Person> — one-arg constructor
            .collect(Collectors.toList());

        System.out.println("  People created from names:");
        people.forEach(p -> System.out.println("    " + p));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Array constructor reference  ClassName[]::new
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Array Constructor Reference ---");

        // Lambda:           n -> new String[n]
        // Constructor ref:  String[]::new

        IntFunction<String[]>  stringArrayFactory  = String[]::new;
        IntFunction<Person[]>  personArrayFactory  = Person[]::new;

        String[] strArr = stringArrayFactory.apply(3);
        Person[] perArr = personArrayFactory.apply(4);

        Arrays.fill(strArr, "default");
        System.out.println("  String[] (size 3): " + Arrays.toString(strArr));
        System.out.println("  Person[] (size 4): length = " + perArr.length);

        // toArray with array constructor reference
        String[] nameArray = names.stream()
            .toArray(String[]::new);   // IntFunction<String[]>
        System.out.println("  names.toArray(String[]::new): " + Arrays.toString(nameArray));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Custom tri-function constructor reference
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Three-Arg Constructor with Custom Functional Interface ---");

        // Java doesn't have TriFunction in the standard library
        // We defined one above

        // Lambda:             (a, b, c) -> new SomeClass(a, b, c)
        // We'll simulate with Person using a helper

        // Person only has up to 2-arg constructor, so let's show the concept
        // with a custom record-like class inline

        TriFunction<String, Integer, String, String> formatter =
            (name, age, city) -> name + " (age " + age + ") from " + city;

        System.out.println("  TriFunction result: " + formatter.apply("Alice", 30, "London"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: Factory pattern using constructor references
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Factory Pattern with Constructor References ---");

        // A simple factory map
        java.util.Map<String, Supplier<List<String>>> factories = new java.util.HashMap<>();
        factories.put("ArrayList", ArrayList::new);
        factories.put("LinkedList", java.util.LinkedList::new);

        for (var entry : factories.entrySet()) {
            List<String> list = entry.getValue().get();
            list.add("item1");
            list.add("item2");
            System.out.println("  " + entry.getKey() + ": " + list);
        }

        // -------------------------------------------------------
        // KEY POINTS:
        // - Syntax: ClassName::new
        // - Replaces: () -> new Foo()            (Supplier)
        //             x  -> new Foo(x)           (Function)
        //             (x,y) -> new Foo(x, y)     (BiFunction)
        // - Compiler picks the constructor that matches the functional interface
        // - Array constructor: int[]::new, String[]::new etc. (IntFunction<T[]>)
        // - Very useful with stream.map() to convert one type to another
        // - Very useful with stream.toArray(Type[]::new)
        // - Enables factory patterns without extra boilerplate
        // -------------------------------------------------------
        System.out.println("\n=== Constructor References Demo Complete ===");
    }
}

