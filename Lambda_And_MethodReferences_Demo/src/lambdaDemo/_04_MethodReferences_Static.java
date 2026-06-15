package lambdaDemo;

import java.util.function.*;
import java.util.Arrays;
import java.util.List;

// ============================================================
// TOPIC: Method References — Type 1: Static Method Reference
// ============================================================
// Syntax:  ClassName::staticMethodName
//
// A static method reference replaces a lambda that does nothing
// but call a static method, passing the lambda's parameter(s)
// directly to it.
//
// Pattern:
//   Lambda:           (x)    -> ClassName.method(x)
//   Method reference: ClassName::method
//
//   Lambda:           (x, y) -> ClassName.method(x, y)
//   Method reference: ClassName::method
// ============================================================

public class _04_MethodReferences_Static {

    // ----------------------------------------------------------
    // Some custom static methods to reference
    // ----------------------------------------------------------

    static String shout(String s)      { return s.toUpperCase() + "!"; }
    static boolean isLongWord(String s) { return s.length() > 4; }
    static void logMessage(String msg)  { System.out.println("  [LOG] " + msg); }
    static String repeat(String s, int times) { return s.repeat(times); }

    public static void main(String[] args) {

        System.out.println("=== Method References — Type 1: Static ===\n");

        // ----------------------------------------------------------
        // Demo 1: Basic static method reference
        //         Lambda vs method reference side by side
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Basic Static Method Reference ---");

        // Lambda form
        Function<String, String> shoutLambda  = s -> shout(s);
        // Method reference form — ClassName::staticMethod
        Function<String, String> shoutRef     = _04_MethodReferences_Static::shout;

        System.out.println("  Lambda  shout(\"hello\") = " + shoutLambda.apply("hello"));
        System.out.println("  MethodRef shout(\"world\") = " + shoutRef.apply("world"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: Static methods from java.lang classes
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Static Methods from Java Library Classes ---");

        // Integer.parseInt(String) — Function<String, Integer>
        Function<String, Integer> parseIntLambda = s -> Integer.parseInt(s);
        Function<String, Integer> parseIntRef    = Integer::parseInt;

        System.out.println("  parseIntLambda(\"42\") = " + parseIntLambda.apply("42"));
        System.out.println("  parseIntRef(\"99\")    = " + parseIntRef.apply("99"));

        // Math.abs(int) — IntUnaryOperator / Function<Integer, Integer>
        Function<Integer, Integer> absLambda = n -> Math.abs(n);
        Function<Integer, Integer> absRef    = Math::abs;

        System.out.println("  absLambda(-7) = " + absLambda.apply(-7));
        System.out.println("  absRef(-42)   = " + absRef.apply(-42));

        // Math.max(int, int) — BiFunction / IntBinaryOperator
        BiFunction<Integer, Integer, Integer> maxLambda = (a, b) -> Math.max(a, b);
        BiFunction<Integer, Integer, Integer> maxRef    = Math::max;

        System.out.println("  maxLambda(3, 7)  = " + maxLambda.apply(3, 7));
        System.out.println("  maxRef(100, 50)  = " + maxRef.apply(100, 50));

        // String.valueOf(Object)
        Function<String, Integer> toStringRef    = String::valueOf;
        System.out.println("  toStringRef(123) = " + toStringRef.apply("123"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Static method references with Predicate
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Static Method Reference as Predicate ---");

        Predicate<String> longWordRef    = _04_MethodReferences_Static::isLongWord;

        List<String> words = Arrays.asList("hi", "hello", "java", "cat", "programming", "go");
        System.out.println("  Words: " + words);
        System.out.print("  Long words (>4 chars): ");
        words.stream().filter(longWordRef).forEach(w -> System.out.print(w + " "));
        System.out.println("\n");

        // ----------------------------------------------------------
        // Demo 4: Static method reference as Consumer
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Static Method Reference as Consumer ---");

        Consumer<String> logLambda = msg -> logMessage(msg);
        Consumer<String> logRef    = _04_MethodReferences_Static::logMessage;

        List<String> events = Arrays.asList("App started", "User logged in", "Request processed");
        System.out.println("  Using lambda:");
        events.forEach(logLambda);
        System.out.println("  Using method reference:");
        events.forEach(logRef);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Two-parameter static method reference (BiFunction)
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Two-Parameter Static Method Reference ---");

        BiFunction<String, Integer, String> repeatLambda = (s, n) -> repeat(s, n);
        BiFunction<String, Integer, String> repeatRef    = _04_MethodReferences_Static::repeat;

        System.out.println("  repeatLambda(\"ha\", 3) = " + repeatLambda.apply("ha", 3));
        System.out.println("  repeatRef(\"la\", 4)    = " + repeatRef.apply("la", 4));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Mixed use — lambdas and method references together
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Mixed Usage in a Pipeline ---");

        List<String> numbers = Arrays.asList("10", "20", "30", "40", "50");
        System.out.println("  Input strings: " + numbers);
        System.out.print("  Parsed & doubled: ");
        numbers.stream()
               .map(Integer::parseInt)          // static method ref: String -> int
               .map(n -> n * 2)                 // lambda: int -> int
               .forEach(System.out::println);   // instance method ref on specific object
        System.out.println();

        // -------------------------------------------------------
        // KEY POINTS:
        // - Syntax: ClassName::staticMethod
        // - Replaces: x -> ClassName.method(x)
        //             (x,y) -> ClassName.method(x,y)
        // - The lambda parameter(s) are passed directly to the static method
        // - Works with any functional interface whose signature matches the method
        // - Common examples: Integer::parseInt, Math::abs, Math::max,
        //                    String::valueOf, Objects::nonNull, Objects::isNull
        // -------------------------------------------------------
        System.out.println("=== Static Method References Demo Complete ===");
    }
}

