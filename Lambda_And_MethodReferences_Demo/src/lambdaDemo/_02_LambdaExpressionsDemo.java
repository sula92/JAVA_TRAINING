package lambdaDemo;

import java.util.function.*;

// ============================================================
// TOPIC: Lambda Expressions — Syntax Deep Dive
// ============================================================
// A lambda expression is an anonymous function — a block of code
// that can be passed around and executed later.
//
// Syntax:
//   (parameters) -> expression              (expression body)
//   (parameters) -> { statements; return x; } (block body)
//
// A lambda implements the SINGLE ABSTRACT METHOD of a
// @FunctionalInterface.
// ============================================================

public class _02_LambdaExpressionsDemo {

    @FunctionalInterface interface Action     { void execute(); }
    @FunctionalInterface interface Transformer { String transform(String s); }
    @FunctionalInterface interface Calculator  { int calculate(int a, int b); }
    @FunctionalInterface interface Validator   { boolean isValid(String s); }
    @FunctionalInterface interface Tripler     { int triple(int a, int b, int c); }

    public static void main(String[] args) {

        System.out.println("=== Lambda Expressions Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Zero-parameter lambda
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Zero Parameters ---");

        // Expression body (single statement)
        Action hello = () -> System.out.println("  Hello from lambda!");

        // Block body (multiple statements)
        Action multiLine = () -> {
            System.out.println("  Line 1 from block-body lambda");
            System.out.println("  Line 2 from block-body lambda");
        };

        hello.execute();
        multiLine.execute();
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: Single-parameter lambda (parentheses optional)
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Single Parameter ---");

        // Parentheses are OPTIONAL for a single parameter
        Transformer shout   = s -> s.toUpperCase();          // no parens
        Transformer whisper = (s) -> s.toLowerCase();        // with parens — also valid
        Transformer addBang = (String s) -> s + "!";         // explicit type — also valid

        System.out.println("  shout(\"hello\")   = " + shout.transform("hello"));
        System.out.println("  whisper(\"HELLO\") = " + whisper.transform("HELLO"));
        System.out.println("  addBang(\"OK\")    = " + addBang.transform("OK"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Multi-parameter lambda
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Multiple Parameters ---");

        Calculator add      = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        Calculator max      = (a, b) -> a > b ? a : b;

        // Explicit types (optional — compiler infers from interface)
        Calculator subtract = (int a, int b) -> a - b;

        System.out.println("  add(5, 3)      = " + add.calculate(5, 3));
        System.out.println("  multiply(5, 3) = " + multiply.calculate(5, 3));
        System.out.println("  max(5, 3)      = " + max.calculate(5, 3));
        System.out.println("  subtract(5, 3) = " + subtract.calculate(5, 3));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Block body with return
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Block Body with return ---");

        Calculator divide = (a, b) -> {
            if (b == 0) {
                System.out.println("  Division by zero!");
                return 0;
            }
            return a / b;
        };

        Validator emailCheck = (email) -> {
            if (email == null || email.isEmpty()) return false;
            return email.contains("@") && email.contains(".");
        };

        System.out.println("  divide(10, 2)  = " + divide.calculate(10, 2));
        System.out.println("  divide(10, 0)  = " + divide.calculate(10, 0));
        System.out.println("  isValid(\"a@b.c\")  = " + emailCheck.isValid("a@b.c"));
        System.out.println("  isValid(\"noAt\")   = " + emailCheck.isValid("noAt"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Lambda assigned to built-in functional interfaces
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Lambda with Built-in Interfaces ---");

        Predicate<Integer>      isEven    = n -> n % 2 == 0;
        Consumer<String>        printer   = s -> System.out.println("  >> " + s);
        Supplier<String>        greeting  = () -> "Hello, World!";
        Function<String, Integer> strLen  = s -> s.length();
        BiFunction<Integer, Integer, Integer> power = (base, exp) -> {
            int result = 1;
            for (int i = 0; i < exp; i++) result *= base;
            return result;
        };

        System.out.println("  isEven(4)         = " + isEven.test(4));
        System.out.println("  isEven(7)         = " + isEven.test(7));
        printer.accept("Consumer lambda");
        System.out.println("  Supplier.get()    = " + greeting.get());
        System.out.println("  length(\"hello\")   = " + strLen.apply("hello"));
        System.out.println("  2^10              = " + power.apply(2, 10));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Type inference — compiler deduces parameter types
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Type Inference ---");

        // The compiler infers types from the functional interface declaration
        // No need to write (String a, String b) — just (a, b) works
        java.util.Comparator<String> byLength = (a, b) -> Integer.compare(a.length(), b.length());
        System.out.println("  compare(\"hi\",\"hello\") = " + byLength.compare("hi", "hello"));

        // Same with generic Function
        Function<String, String> trim = s -> s.trim();
        System.out.println("  trim(\"  spaces  \") = \"" + trim.apply("  spaces  ") + "\"");
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: Lambda returning another lambda (higher-order functions)
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Lambda Returning a Lambda ---");

        // A function that creates a "multiplier" function
        Function<Integer, Function<Integer, Integer>> multiplierFactory =
                factor -> (number -> number * factor);

        Function<Integer, Integer> triple = multiplierFactory.apply(3);
        Function<Integer, Integer> double_ = multiplierFactory.apply(2);

        System.out.println("  triple.apply(5)  = " + triple.apply(5));
        System.out.println("  double_.apply(5) = " + double_.apply(5));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 8: Lambda stored in a variable vs passed inline
        // ----------------------------------------------------------
        System.out.println("--- Demo 8: Stored vs Inline Lambda ---");

        // Stored in variable — reusable
        Predicate<String> notEmpty = s -> !s.isEmpty();
        System.out.println("  Stored lambda — notEmpty(\"\")    = " + notEmpty.test(""));
        System.out.println("  Stored lambda — notEmpty(\"hi\")  = " + notEmpty.test("hi"));

        // Inline — single-use
        System.out.println("  Inline lambda — isEven(6) = "
                + ((Predicate<Integer>) n -> n % 2 == 0).test(6));

        // -------------------------------------------------------
        // KEY POINTS:
        // - Lambda = (params) -> expression  OR  (params) -> { block; }
        // - Single param: parens optional;  zero or multi: parens required
        // - Block body requires explicit 'return'; expression body is implicit
        // - Explicit type annotations are optional (compiler infers from interface)
        // - Lambda implements the ONE abstract method of a @FunctionalInterface
        // - 'this' inside a lambda = ENCLOSING class instance
        // - A lambda is NOT an object — it uses invokedynamic bytecode
        // -------------------------------------------------------
        System.out.println("\n=== Lambda Expressions Demo Complete ===");
    }
}

