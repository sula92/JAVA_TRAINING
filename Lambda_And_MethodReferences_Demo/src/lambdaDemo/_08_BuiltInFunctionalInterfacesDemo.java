package lambdaDemo;

import java.util.function.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// ============================================================
// TOPIC: Built-in Functional Interfaces (java.util.function)
// ============================================================
// Java 8 ships a rich set of ready-made functional interfaces so
// you rarely need to define your own.
//
// Core four:
//   Predicate<T>        test(T) → boolean         (test a condition)
//   Consumer<T>         accept(T) → void           (consume a value)
//   Supplier<T>         get() → T                  (produce a value)
//   Function<T,R>       apply(T) → R               (transform a value)
//
// Two-arg variants:
//   BiPredicate<T,U>    test(T,U) → boolean
//   BiConsumer<T,U>     accept(T,U) → void
//   BiFunction<T,U,R>   apply(T,U) → R
//
// Same-type variants:
//   UnaryOperator<T>    apply(T) → T               extends Function<T,T>
//   BinaryOperator<T>   apply(T,T) → T             extends BiFunction<T,T,T>
//
// Primitive specialisations (avoid boxing overhead):
//   IntPredicate, IntConsumer, IntSupplier, IntFunction<R>,
//   IntUnaryOperator, IntBinaryOperator  (also Long and Double variants)
// ============================================================

public class _08_BuiltInFunctionalInterfacesDemo {

    public static void main(String[] args) {

        System.out.println("=== Built-in Functional Interfaces Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Predicate<T> — boolean test(T t)
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Predicate<T> ---");

        Predicate<Integer> isPositive  = n -> n > 0;
        Predicate<Integer> isEven      = n -> n % 2 == 0;
        Predicate<String>  isLongStr   = s -> s.length() > 5;
        Predicate<String>  startWithA  = s -> s.startsWith("A");

        System.out.println("  isPositive.test(5)  = " + isPositive.test(5));
        System.out.println("  isPositive.test(-3) = " + isPositive.test(-3));
        System.out.println("  isEven.test(4)      = " + isEven.test(4));

        // Combining predicates
        Predicate<Integer> positiveEven = isPositive.and(isEven);
        Predicate<Integer> positiveOrEven = isPositive.or(isEven);
        Predicate<Integer> notPositive  = isPositive.negate();

        System.out.println("  positiveEven.test(4)   = " + positiveEven.test(4));   // T&&T
        System.out.println("  positiveEven.test(-2)  = " + positiveEven.test(-2));  // F&&T
        System.out.println("  positiveOrEven.test(-2)= " + positiveOrEven.test(-2));// F||T
        System.out.println("  notPositive.test(-5)   = " + notPositive.test(-5));

        // Filter a list
        List<Integer> numbers = Arrays.asList(-5, -2, 0, 3, 4, 7, 10, 12);
        System.out.print("  Numbers (filter positiveEven): ");
        numbers.stream().filter(positiveEven).forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // ----------------------------------------------------------
        // Demo 2: Consumer<T> — void accept(T t)
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Consumer<T> ---");

        Consumer<String>  print      = s -> System.out.println("  PRINT: " + s);
        Consumer<String>  log        = s -> System.out.println("  LOG:   " + s);
        Consumer<Integer> printSquare = n -> System.out.println("  Square of " + n + " = " + (n * n));

        print.accept("Hello Consumer!");
        printSquare.accept(7);

        // Consumer chaining with andThen
        Consumer<String> printThenLog = print.andThen(log);
        System.out.println("  --- andThen chain: ---");
        printThenLog.accept("chained message");

        // forEach uses Consumer
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        System.out.println("  forEach with Consumer:");
        names.forEach(print);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Supplier<T> — T get()
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Supplier<T> ---");

        Supplier<String>  greeting     = () -> "Hello, World!";
        Supplier<Double>  randomDouble = Math::random;
        Supplier<List<String>> emptyList = java.util.ArrayList::new;

        System.out.println("  greeting.get()      = " + greeting.get());
        System.out.println("  randomDouble.get()  = " + randomDouble.get());
        System.out.println("  emptyList.get()     = " + emptyList.get());

        // Lazy initialization — only compute when needed
        Supplier<String> expensive = () -> {
            // simulate expensive computation
            return "Result after heavy computation";
        };
        System.out.println("  Lazy supplier: " + expensive.get());

        // Optional.orElseGet uses Supplier
        Optional<String> empty = Optional.empty();
        String fallback = empty.orElseGet(() -> "Default Value");
        System.out.println("  Optional.orElseGet: " + fallback);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Function<T,R> — R apply(T t)
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Function<T,R> ---");

        Function<String, Integer>  strToLen   = String::length;
        Function<String, String>   toUpper    = String::toUpperCase;
        Function<Integer, String>  intToStr   = Object::toString;
        Function<Integer, Integer> square     = n -> n * n;

        System.out.println("  strToLen(\"Hello\")  = " + strToLen.apply("Hello"));
        System.out.println("  toUpper(\"java\")    = " + toUpper.apply("java"));
        System.out.println("  square(9)          = " + square.apply(9));

        // Function chaining: andThen and compose
        Function<Integer, Integer> doubleIt  = n -> n * 2;
        Function<Integer, Integer> addTen    = n -> n + 10;

        Function<Integer, Integer> doubleThenAddTen = doubleIt.andThen(addTen);
        Function<Integer, Integer> addTenThenDouble = doubleIt.compose(addTen);
        // andThen: doubleIt FIRST, then addTen
        // compose: addTen FIRST, then doubleIt

        System.out.println("  doubleThenAddTen(5): double(5)=10, +10=20 → " + doubleThenAddTen.apply(5));
        System.out.println("  addTenThenDouble(5): +10=15, double(15)=30 → " + addTenThenDouble.apply(5));

        // identity() — returns a function that returns its input unchanged
        Function<String, String> identity = Function.identity();
        System.out.println("  Function.identity()(\"hello\") = " + identity.apply("hello"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: BiFunction<T,U,R> — R apply(T t, U u)
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: BiFunction<T,U,R> ---");

        BiFunction<String, Integer, String> repeat  = (s, n) -> s.repeat(n);
        BiFunction<Integer, Integer, Integer> power = (base, exp) -> {
            int result = 1;
            for (int i = 0; i < exp; i++) result *= base;
            return result;
        };
        BiFunction<String, String, String> concat = (a, b) -> a + " " + b;

        System.out.println("  repeat(\"ha\", 3)   = " + repeat.apply("ha", 3));
        System.out.println("  power(2, 8)        = " + power.apply(2, 8));
        System.out.println("  concat(\"Hello\",\"World\") = " + concat.apply("Hello", "World"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: UnaryOperator<T> — T apply(T t)  (Function<T,T>)
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: UnaryOperator<T> ---");

        UnaryOperator<String>  trim        = String::trim;
        UnaryOperator<Integer> negate      = n -> -n;
        UnaryOperator<String>  addExclaim  = s -> s + "!";

        System.out.println("  trim(\"  hello  \")   = \"" + trim.apply("  hello  ") + "\"");
        System.out.println("  negate(42)           = " + negate.apply(42));
        System.out.println("  addExclaim(\"wow\")   = " + addExclaim.apply("wow"));

        // UnaryOperator.identity()
        UnaryOperator<String> id = UnaryOperator.identity();
        System.out.println("  identity(\"test\")    = " + id.apply("test"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: BinaryOperator<T> — T apply(T t1, T t2) (BiFunction<T,T,T>)
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: BinaryOperator<T> ---");

        BinaryOperator<Integer> add         = Integer::sum;    // method ref to static
        BinaryOperator<Integer> maxOp       = Integer::max;
        BinaryOperator<String>  joinWith    = (a, b) -> a + ", " + b;

        System.out.println("  add(3, 7)            = " + add.apply(3, 7));
        System.out.println("  maxOp(9, 4)          = " + maxOp.apply(9, 4));
        System.out.println("  joinWith(\"a\",\"b\")     = " + joinWith.apply("a", "b"));

        // reduce uses BinaryOperator
        List<Integer> vals = Arrays.asList(1, 2, 3, 4, 5);
        int sum = vals.stream().reduce(0, Integer::sum);
        System.out.println("  reduce sum of [1,2,3,4,5] = " + sum);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 8: Primitive specialisations (avoid boxing overhead)
        // ----------------------------------------------------------
        System.out.println("--- Demo 8: Primitive Specialisations ---");

        IntPredicate     isEvenInt   = n -> n % 2 == 0;
        IntConsumer      printInt    = n -> System.out.println("  IntConsumer: " + n);
        IntSupplier      always42    = () -> 42;
        IntUnaryOperator tripleInt   = n -> n * 3;
        IntBinaryOperator sumInts    = Integer::sum;

        System.out.println("  isEvenInt.test(4)   = " + isEvenInt.test(4));
        printInt.accept(99);
        System.out.println("  always42.getAsInt() = " + always42.getAsInt());
        System.out.println("  tripleInt(7)        = " + tripleInt.applyAsInt(7));
        System.out.println("  sumInts(3, 4)       = " + sumInts.applyAsInt(3, 4));

        // -------------------------------------------------------
        // KEY POINTS:
        // - Predicate<T>   → test(); use .and() .or() .negate() to combine
        // - Consumer<T>    → accept(); use .andThen() to chain actions
        // - Supplier<T>    → get(); used for lazy init and factories
        // - Function<T,R>  → apply(); use .andThen() (after) .compose() (before)
        // - BiFunction     → two inputs; BiPredicate, BiConsumer also available
        // - UnaryOperator  → same-type Function; BinaryOperator → same-type BiFunction
        // - Primitive variants (Int/Long/Double) avoid boxing for performance
        // -------------------------------------------------------
        System.out.println("\n=== Built-in Functional Interfaces Demo Complete ===");
    }
}

