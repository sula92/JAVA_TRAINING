package lambdaDemo;

import java.util.function.*;
import java.util.Arrays;
import java.util.List;

// ============================================================
// TOPIC: Chaining Functional Interfaces
// ============================================================
// Many functional interfaces provide DEFAULT METHODS for chaining:
//
// Predicate:
//   .and(other)    → this AND other  (short-circuit)
//   .or(other)     → this OR other   (short-circuit)
//   .negate()      → NOT this
//   Predicate.not(p) → NOT p         (Java 11+, static)
//
// Function:
//   .andThen(after) → apply this, THEN apply 'after'
//   .compose(before)→ apply 'before' FIRST, then this
//   Function.identity() → returns its input unchanged
//
// Consumer:
//   .andThen(after) → accept with this, THEN accept with 'after'
//
// Comparator:
//   .thenComparing(other) → first by this, then by other
//   .reversed()           → reverse the ordering
//   Comparator.comparing(keyExtractor) → sort by extracted key
// ============================================================

public class _09_FunctionChainingDemo {

    static class Employee {
        String name;
        String dept;
        double salary;

        Employee(String name, String dept, double salary) {
            this.name   = name;
            this.dept   = dept;
            this.salary = salary;
        }

        String getName()   { return name; }
        String getDept()   { return dept; }
        double getSalary() { return salary; }

        @Override
        public String toString() {
            return name + " [" + dept + ", $" + salary + "]";
        }
    }

    public static void main(String[] args) {

        System.out.println("=== Function Chaining Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Predicate chaining — and / or / negate
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Predicate Chaining ---");

        Predicate<Integer> isPositive   = n -> n > 0;
        Predicate<Integer> isEven       = n -> n % 2 == 0;
        Predicate<Integer> isLessThan10 = n -> n < 10;

        Predicate<Integer> positiveAndEven          = isPositive.and(isEven);
        Predicate<Integer> positiveOrEven           = isPositive.or(isEven);
        Predicate<Integer> notPositive              = isPositive.negate();
        Predicate<Integer> positiveEvenLessThan10   = isPositive.and(isEven).and(isLessThan10);

        int[] testNums = {-4, -1, 0, 2, 5, 8, 12};
        System.out.println("  n        positive  even    pos&&even  pos||even  !positive  pos&&even&&<10");
        for (int n : testNums) {
            System.out.printf("  %-8d %-9s %-7s %-10s %-10s %-10s %s%n",
                n,
                isPositive.test(n), isEven.test(n),
                positiveAndEven.test(n), positiveOrEven.test(n),
                notPositive.test(n), positiveEvenLessThan10.test(n));
        }

        // Predicate.not() — Java 11+
        Predicate<String> nonEmpty = Predicate.not(String::isEmpty);
        List<String> strings = Arrays.asList("hello", "", "world", "  ", "java");
        System.out.print("\n  Non-empty strings: ");
        strings.stream().filter(nonEmpty).forEach(s -> System.out.print("\"" + s + "\" "));
        System.out.println("\n");

        // ----------------------------------------------------------
        // Demo 2: Function.andThen() — apply THIS first, then AFTER
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Function.andThen() ---");

        Function<Integer, Integer> multiplyBy3 = n -> n * 3;
        Function<Integer, Integer> addTen      = n -> n + 10;
        Function<Integer, String>  intToStr    = n -> "Result: " + n;

        // Chain: multiplyBy3 → addTen → intToStr
        Function<Integer, String> pipeline =
            multiplyBy3.andThen(addTen).andThen(intToStr);

        System.out.println("  Input 5: multiplyBy3 → addTen → intToStr");
        System.out.println("    Step 1: 5 × 3 = 15");
        System.out.println("    Step 2: 15 + 10 = 25");
        System.out.println("    Step 3: \"Result: 25\"");
        System.out.println("    pipeline.apply(5) = " + pipeline.apply(5));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Function.compose() — apply BEFORE first, then THIS
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Function.compose() ---");

        // f.compose(g) means: apply g FIRST, then f
        // f.andThen(g) means: apply f FIRST, then g
        // So: f.andThen(g) == g.compose(f)

        Function<Integer, Integer> addFive  = n -> n + 5;
        Function<Integer, Integer> double_  = n -> n * 2;

        Function<Integer, Integer> addThenDouble = double_.compose(addFive); // addFive first, then double_
        Function<Integer, Integer> doubleThenAdd = double_.andThen(addFive); // double_ first, then addFive

        System.out.println("  Input 3:");
        System.out.println("    compose (addFive first): 3+5=8, 8*2=16 → " + addThenDouble.apply(3));
        System.out.println("    andThen (double first) : 3*2=6, 6+5=11 → " + doubleThenAdd.apply(3));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Consumer.andThen() — run both consumers in sequence
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Consumer.andThen() ---");

        Consumer<String> printMsg  = s -> System.out.println("  [PRINT] " + s);
        Consumer<String> logMsg    = s -> System.out.println("  [LOG]   " + s);
        Consumer<String> alertMsg  = s -> System.out.println("  [ALERT] " + s.toUpperCase());

        // Chain three consumers — all run in order
        Consumer<String> printAndLog = printMsg.andThen(logMsg);
        Consumer<String> fullPipeline = printMsg.andThen(logMsg).andThen(alertMsg);

        System.out.println("  printAndLog.accept(\"error\"):");
        printAndLog.accept("error");
        System.out.println("  fullPipeline.accept(\"warning\"):");
        fullPipeline.accept("warning");
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Building a data transformation pipeline
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Data Transformation Pipeline ---");

        Function<String, String>  trimInput      = String::trim;
        Function<String, String>  normalizeCase  = String::toLowerCase;
        Function<String, Boolean> isValidEmail   = s -> s.contains("@") && s.contains(".");
        Function<String, String>  wrapInTag      = s -> "<email>" + s + "</email>";

        // Build pipeline: trim → normalize → validate (as a full Function chain)
        Function<String, String> cleanEmail = trimInput.andThen(normalizeCase);

        String[] emails = {"  Alice@Example.COM  ", "BOB@DOMAIN.ORG", " invalid ", "test@test.co"};
        System.out.println("  Email normalization pipeline:");
        for (String email : emails) {
            String cleaned = cleanEmail.apply(email);
            boolean valid  = isValidEmail.apply(cleaned);
            System.out.printf("    %-25s → %-22s valid=%s%n",
                    "\"" + email.trim() + "\"", "\"" + cleaned + "\"", valid);
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Combining Predicates for complex filtering
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Complex Predicate Combination for Filtering ---");

        List<Employee> employees = Arrays.asList(
            new Employee("Alice",   "Engineering",  95000),
            new Employee("Bob",     "Marketing",    55000),
            new Employee("Charlie", "Engineering",  72000),
            new Employee("Diana",   "HR",           48000),
            new Employee("Eve",     "Engineering", 110000),
            new Employee("Frank",   "Marketing",    62000)
        );

        Predicate<Employee> isEngineer   = e -> e.getDept().equals("Engineering");
        Predicate<Employee> highSalary   = e -> e.getSalary() > 80000;
        Predicate<Employee> isMarketing  = e -> e.getDept().equals("Marketing");

        System.out.println("  High-salary engineers (Eng AND salary > 80k):");
        employees.stream()
                 .filter(isEngineer.and(highSalary))
                 .forEach(e -> System.out.println("    " + e));

        System.out.println("  Engineers OR Marketing:");
        employees.stream()
                 .filter(isEngineer.or(isMarketing))
                 .forEach(e -> System.out.println("    " + e));

        System.out.println("  NOT engineers (non-engineering):");
        employees.stream()
                 .filter(isEngineer.negate())
                 .forEach(e -> System.out.println("    " + e));

        // -------------------------------------------------------
        // KEY POINTS:
        // - Predicate.and/or/negate → combine boolean conditions
        // - Function.andThen(f)  → this THEN f   (left-to-right pipeline)
        // - Function.compose(f)  → f THEN this   (right-to-left)
        // - Consumer.andThen(c)  → run this, then c (sequential side effects)
        // - All chaining returns a NEW functional interface; originals unchanged
        // - Great for building reusable, composable business logic
        // -------------------------------------------------------
        System.out.println("\n=== Function Chaining Demo Complete ===");
    }
}

