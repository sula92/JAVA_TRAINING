package lambdaDemo;

import java.util.function.*;
import java.util.Arrays;
import java.util.List;

// ============================================================
// TOPIC: Method References — Type 2: Specific Instance Method Reference
// ============================================================
// Syntax:  objectReference::instanceMethodName
//
// The lambda calls an instance method on a SPECIFIC, ALREADY-KNOWN
// object (the reference is captured at the time the method reference
// is created).
//
// Pattern:
//   Lambda:           ()  -> specificObject.method()
//   Method reference: specificObject::method
//
//   Lambda:           (x) -> specificObject.method(x)
//   Method reference: specificObject::method
//
// The most common example is: System.out::println
//   System.out is a specific PrintStream instance.
//   println() is called on that exact instance.
// ============================================================

public class _05_MethodReferences_SpecificInstance {

    // Custom class to demonstrate instance method references
    static class Formatter {
        private final String prefix;
        private final String suffix;

        Formatter(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public String format(String s) {
            return prefix + s + suffix;
        }

        public boolean isLongerThan(String s, int threshold) {
            return s.length() > threshold;
        }

        public void log(String message) {
            System.out.println("  [" + prefix + "] " + message);
        }
    }

    static class Counter {
        private int count = 0;
        public int increment() { return ++count; }
        public int getCount()  { return count; }
    }

    public static void main(String[] args) {

        System.out.println("=== Method References — Type 2: Specific Instance ===\n");

        // ----------------------------------------------------------
        // Demo 1: The classic example — System.out::println
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: System.out::println (most common example) ---");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana");

        // Lambda form
        names.forEach(name -> System.out.println("  " + name));
        System.out.println();

        // System.out is a specific PrintStream instance.
        // println is called on THAT specific instance.
        Consumer<String> printRef = System.out::println;
        System.out.println("  Using System.out::println directly:");
        names.forEach(System.out::println);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: Method reference on a custom object
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Method Reference on a Custom Object ---");

        Formatter htmlFormatter   = new Formatter("<b>", "</b>");
        Formatter bracketFormatter = new Formatter("[", "]");

        // Lambda form
        Function<String, String> htmlLambda    = s -> htmlFormatter.format(s);
        // Method reference form — captures 'htmlFormatter' as the specific instance
        Function<String, String> htmlRef       = htmlFormatter::format;
        Function<String, String> bracketRef    = bracketFormatter::format;

        System.out.println("  htmlRef.apply(\"hello\")    = " + htmlRef.apply("hello"));
        System.out.println("  bracketRef.apply(\"world\") = " + bracketRef.apply("world"));

        List<String> words = Arrays.asList("apple", "banana", "cherry");
        System.out.println("  HTML-formatted list:");
        words.stream().map(htmlRef).forEach(s -> System.out.println("    " + s));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Consumer method reference — void method on specific object
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Consumer Method Reference ---");

        Formatter infoLogger  = new Formatter("INFO", "");
        Formatter errorLogger = new Formatter("ERROR", "");

        Consumer<String> infoLog  = infoLogger::log;
        Consumer<String> errorLog = errorLogger::log;

        infoLog.accept("Application started");
        infoLog.accept("Processing request");
        errorLog.accept("File not found");
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Supplier method reference — no-arg method on specific object
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Supplier Method Reference ---");

        Counter counter = new Counter();

        // Lambda: () -> counter.increment()
        // The lambda captures 'counter' as the specific instance
        Supplier<Integer> incrementRef = counter::increment;

        System.out.println("  Calling increment 3 times via Supplier method reference:");
        System.out.println("  Result: " + incrementRef.get());
        System.out.println("  Result: " + incrementRef.get());
        System.out.println("  Result: " + incrementRef.get());
        System.out.println("  Final count: " + counter.getCount());
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: StringBuilder instance methods
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: StringBuilder Instance Method Reference ---");

        StringBuilder sb = new StringBuilder("Hello");

        // Consumer<String> — calls sb.append(str) on THIS specific sb instance
        Consumer<String> appender = sb::append;

        appender.accept(", ");
        appender.accept("World");
        appender.accept("!");
        System.out.println("  StringBuilder result: " + sb.toString());

        // Supplier<String> — calls sb.toString() on THIS specific sb instance
        Supplier<String> toStringRef = sb::toString;
        System.out.println("  Supplier<String>: " + toStringRef.get());
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Two method references on DIFFERENT instances
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Different Instances — Different Behaviours ---");

        Formatter upperCase = new Formatter("", "".toUpperCase());
        Formatter starred   = new Formatter("*** ", " ***");

        Function<String, String> upperRef   = upperCase::format;
        Function<String, String> starredRef = starred::format;

        String message = "important";
        System.out.println("  upperRef.apply(\"" + message + "\")   = " + upperRef.apply(message));
        System.out.println("  starredRef.apply(\"" + message + "\") = " + starredRef.apply(message));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: Method reference in stream pipeline
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Specific Instance Ref in Stream Pipeline ---");

        StringBuilder collector = new StringBuilder();
        Consumer<String> collectRef = s -> collector.append(s).append(", ");

        List<String> fruits = Arrays.asList("apple", "mango", "banana", "kiwi");
        fruits.forEach(collectRef);

        // Remove trailing ", "
        String result = collector.substring(0, collector.length() - 2);
        System.out.println("  Collected: " + result);

        // -------------------------------------------------------
        // KEY POINTS:
        // - Syntax: specificObject::instanceMethod
        // - The object is CAPTURED when the method reference is created
        // - Every invocation calls the method on THAT SAME specific object
        // - Most common example: System.out::println
        // - Works for Consumer (void methods), Supplier (no-arg methods),
        //   Function (one-arg methods), BiFunction (two-arg methods)
        // -------------------------------------------------------
        System.out.println("\n=== Specific Instance Method References Demo Complete ===");
    }
}

